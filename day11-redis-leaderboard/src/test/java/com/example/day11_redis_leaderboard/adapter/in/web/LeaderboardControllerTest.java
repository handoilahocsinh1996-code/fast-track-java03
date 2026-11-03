
package com.example.day11_redis_leaderboard.adapter.in.web;

import com.example.day11_redis_leaderboard.application.port.in.GetTopPlayersUseCase;
import com.example.day11_redis_leaderboard.application.port.in.UpdateScoreUseCase;
import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaderboardController.class)
class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UpdateScoreUseCase updateScoreUseCase;

    @MockitoBean
    private GetTopPlayersUseCase getTopPlayersUseCase;

    @Test
    void shouldUpdateScoreSuccessfully() throws Exception {
        mockMvc.perform(post("/leaderboard/alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "score": 150
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(updateScoreUseCase).updateScore("alice", 150.0);
    }

    @Test
    void shouldReturnTopPlayers() throws Exception {
        when(getTopPlayersUseCase.getTopPlayers(2))
                .thenReturn(List.of(
                        new LeaderboardEntry("alice", 300.0, 1),
                        new LeaderboardEntry("bob", 200.0, 2)
                ));

        mockMvc.perform(get("/leaderboard/top/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("alice"))
                .andExpect(jsonPath("$[0].score").value(300.0))
                .andExpect(jsonPath("$[0].rank").value(1))
                .andExpect(jsonPath("$[1].username").value("bob"))
                .andExpect(jsonPath("$[1].rank").value(2));
    }

    @Test
    void shouldRejectNegativeScore() throws Exception {
        mockMvc.perform(post("/leaderboard/alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "score": -10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectMissingScore() throws Exception {
        mockMvc.perform(post("/leaderboard/alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectZeroLimit() throws Exception {
        mockMvc.perform(get("/leaderboard/top/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNegativeLimit() throws Exception {
        mockMvc.perform(get("/leaderboard/top/-1"))
                .andExpect(status().isBadRequest());
    }
}