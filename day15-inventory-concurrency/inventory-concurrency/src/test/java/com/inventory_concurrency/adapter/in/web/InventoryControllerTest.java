
package com.inventory_concurrency.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_concurrency.application.dto.InventoryHistoryResponse;
import com.inventory_concurrency.application.dto.InventoryResponse;
import com.inventory_concurrency.application.dto.StockInRequest;
import com.inventory_concurrency.application.dto.UpdateInventoryRequest;
import com.inventory_concurrency.application.port.in.InventoryUseCase;
import com.inventory_concurrency.application.service.InventoryNotFoundException;
import com.inventory_concurrency.application.service.InventoryVersionConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryUseCase inventoryUseCase;

    @Test
    void stockIn_shouldReturnUpdatedInventory() throws Exception {
        Long productId = 1L;

        InventoryResponse response = new InventoryResponse(productId, "Keyboard", 15, 1L);

        when(inventoryUseCase.stockIn(eq(productId), any(StockInRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/inventories/{productId}/stock-in", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new StockInRequest(5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Keyboard"))
                .andExpect(jsonPath("$.quantity").value(15))
                .andExpect(jsonPath("$.version").value(1));
    }

    @Test
    void stockIn_shouldReturnBadRequestWhenQuantityIsInvalid() throws Exception {
        mockMvc.perform(post("/inventories/{productId}/stock-in", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"quantity": 0}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verifyNoInteractions(inventoryUseCase);
    }

    @Test
    void updateInventory_shouldReturnUpdatedInventory() throws Exception {
        Long productId = 1L;

        UpdateInventoryRequest request = new UpdateInventoryRequest("Keyboard Pro", 20, 2L);

        InventoryResponse response = new InventoryResponse(productId, "Keyboard Pro", 20, 3L);

        when(inventoryUseCase.updateInventory(
                eq(productId), any(UpdateInventoryRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/inventories/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Keyboard Pro"))
                .andExpect(jsonPath("$.quantity").value(20))
                .andExpect(jsonPath("$.version").value(3));
    }

    @Test
    void updateInventory_shouldReturnConflictWhenVersionConflicts() throws Exception {
        Long productId = 1L;

        when(inventoryUseCase.updateInventory(
                eq(productId), any(UpdateInventoryRequest.class)))
                .thenThrow(new InventoryVersionConflictException(
                        "Inventory has been modified. Please refresh and try again."));

        mockMvc.perform(put("/inventories/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "productName": "Keyboard Pro",
                            "quantity": 20,
                            "version": 2
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getInventoryHistory_shouldReturnHistory() throws Exception {
        Long productId = 1L;

        List<InventoryHistoryResponse> history = List.of(
                new InventoryHistoryResponse(
                        1L, Instant.parse("2026-01-01T10:00:00Z"),
                        "Keyboard", 10, "ADD"),
                new InventoryHistoryResponse(
                        2L, Instant.parse("2026-01-02T10:00:00Z"),
                        "Keyboard", 15, "MOD"));

        when(inventoryUseCase.getInventoryHistory(productId))
                .thenReturn(history);

        mockMvc.perform(get("/inventories/{productId}/history", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].quantity").value(15));
    }

    @Test
    void getInventoryHistory_shouldReturnNotFoundWhenInventoryDoesNotExist()
            throws Exception {
        Long productId = 999L;

        when(inventoryUseCase.getInventoryHistory(productId))
                .thenThrow(new InventoryNotFoundException(productId));

        mockMvc.perform(get("/inventories/{productId}/history", productId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
