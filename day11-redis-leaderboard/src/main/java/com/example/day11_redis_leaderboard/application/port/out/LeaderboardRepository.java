
package com.example.day11_redis_leaderboard.application.port.out;

import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;

import java.util.List;

public interface LeaderboardRepository {

    /**
     * Creates or updates a player's score.
     *
     * @param username player's username
     * @param score player's score
     */
    void updateScore(String username, double score);

    /**
     * Retrieves the top players ordered by score from highest to lowest.
     *
     * @param limit maximum number of players to return
     * @return players ordered by descending score
     */
    List<LeaderboardEntry> findTopPlayers(int limit);
}