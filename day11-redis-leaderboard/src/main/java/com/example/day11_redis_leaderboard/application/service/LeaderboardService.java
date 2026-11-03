
package com.example.day11_redis_leaderboard.application.service;

import com.example.day11_redis_leaderboard.application.port.in.GetTopPlayersUseCase;
import com.example.day11_redis_leaderboard.application.port.in.UpdateScoreUseCase;
import com.example.day11_redis_leaderboard.application.port.out.LeaderboardRepository;
import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardService
        implements UpdateScoreUseCase, GetTopPlayersUseCase {

    private final LeaderboardRepository leaderboardRepository;

    public LeaderboardService(LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }

    @Override
    public void updateScore(String username, double score) {
        leaderboardRepository.updateScore(username, score);
    }

    @Override
    public List<LeaderboardEntry> getTopPlayers(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "Limit must be greater than zero"
            );
        }

        return leaderboardRepository.findTopPlayers(limit);
    }
}