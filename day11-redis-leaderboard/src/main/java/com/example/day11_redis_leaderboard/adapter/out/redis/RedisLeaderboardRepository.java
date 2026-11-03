
package com.example.day11_redis_leaderboard.adapter.out.redis;

import com.example.day11_redis_leaderboard.application.port.out.LeaderboardRepository;
import com.example.day11_redis_leaderboard.domain.model.LeaderboardEntry;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class RedisLeaderboardRepository implements LeaderboardRepository {

    private static final String LEADERBOARD_KEY = "leaderboard";

    private final StringRedisTemplate redisTemplate;

    public RedisLeaderboardRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void updateScore(String username, double score) {
        redisTemplate.opsForZSet()
                .add(LEADERBOARD_KEY, username, score);
    }

    @Override
    public List<LeaderboardEntry> findTopPlayers(int limit) {
        Set<ZSetOperations.TypedTuple<String>> tuples =
                redisTemplate.opsForZSet()
                        .reverseRangeWithScores(LEADERBOARD_KEY, 0, limit - 1L);

        List<LeaderboardEntry> entries = new ArrayList<>();

        if (tuples == null || tuples.isEmpty()) {
            return entries;
        }

        long rank = 1;

        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String username = tuple.getValue();
            Double score = tuple.getScore();

            if (username != null && score != null) {
                entries.add(new LeaderboardEntry(username, score, rank++));
            }
        }

        return entries;
    }
}