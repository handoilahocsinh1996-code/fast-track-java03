
package com.example.day11_redis_leaderboard.application.port.in;

import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;

import java.util.List;

public interface GetTopPlayersUseCase {

    List<LeaderboardEntry> getTopPlayers(int limit);
}