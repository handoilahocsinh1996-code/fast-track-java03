package com.day12_multi_level_caching.adapter.in.web;

import com.day12_multi_level_caching.adapter.in.web.dto.ProductCreateRequest;
import com.day12_multi_level_caching.adapter.in.web.dto.ProductResponse;
import com.day12_multi_level_caching.application.port.in.CreateProductUseCase;
import com.day12_multi_level_caching.application.port.in.DeleteProductUseCase;
import com.day12_multi_level_caching.application.port.in.GetProductUseCase;
import com.day12_multi_level_caching.domain.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public ProductController(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase,
            DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        Product product = createProductUseCase.createProduct(
                request.name(),
                request.price());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ProductResponse.from(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {
        Product product = getProductUseCase.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found: " + id));

        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @PathVariable Long id) {
        boolean deleted = deleteProductUseCase.deleteProductById(id);

        if (!deleted) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found: " + id);
        }

        return ResponseEntity.noContent().build();
    }
}