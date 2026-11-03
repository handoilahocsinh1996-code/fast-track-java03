
package com.example.day11_redis_leaderboard.application.service;

import com.example.day11_redis_leaderboard.application.port.out.LeaderboardRepository;
import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaderboardServiceTest {

    @Mock
    private LeaderboardRepository leaderboardRepository;

    private LeaderboardService leaderboardService;

    @BeforeEach
    void setUp() {
        leaderboardService = new LeaderboardService(leaderboardRepository);
    }

    @Test
    void shouldUpdatePlayerScore() {
        // Given
        String username = "alice";
        double score = 150.0;

        // When
        leaderboardService.updateScore(username, score);

        // Then
        verify(leaderboardRepository).updateScore(username, score);
    }

    @Test
    void shouldReturnTopPlayers() {
        // Given
        List<LeaderboardEntry> expected = List.of(
                new LeaderboardEntry("alice", 300.0, 1),
                new LeaderboardEntry("bob", 200.0, 2),
                new LeaderboardEntry("charlie", 100.0, 3)
        );

        when(leaderboardRepository.findTopPlayers(3))
                .thenReturn(expected);

        // When
        List<LeaderboardEntry> actual =
                leaderboardService.getTopPlayers(3);

        // Then
        assertEquals(expected, actual);
        verify(leaderboardRepository).findTopPlayers(3);
    }

    @Test
    void shouldThrowExceptionWhenLimitIsZero() {
        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> leaderboardService.getTopPlayers(0)
        );
    }

    @Test
    void shouldThrowExceptionWhenLimitIsNegative() {
        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> leaderboardService.getTopPlayers(-1)
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoPlayersExist() {
        // Given
        when(leaderboardRepository.findTopPlayers(5))
                .thenReturn(List.of());

        // When
        List<LeaderboardEntry> actual =
                leaderboardService.getTopPlayers(5);

        // Then
        assertEquals(List.of(), actual);
    }
}