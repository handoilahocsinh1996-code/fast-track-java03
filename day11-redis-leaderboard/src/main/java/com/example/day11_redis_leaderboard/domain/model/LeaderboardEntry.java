package com.example.day11_redis_leaderboard.domain.model;

public record LeaderboardEntry(
        String username,
        double score,
        long rank
) {
}
