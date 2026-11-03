
package com.example.day11_redis_leaderboard.application.port.in;

public interface UpdateScoreUseCase {

    void updateScore(String username, double score);
}