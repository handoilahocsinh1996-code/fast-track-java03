
package com.example.day11_redis_leaderboard.adapter.in.web.dto;

public record LeaderboardResponse(
        String username,
        double score,
        long rank
) {
}