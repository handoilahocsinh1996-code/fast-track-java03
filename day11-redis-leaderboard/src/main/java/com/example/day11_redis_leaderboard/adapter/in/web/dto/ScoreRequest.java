
package com.example.day11_redis_leaderboard.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ScoreRequest(

        @NotNull(message = "Score must not be null")
        @PositiveOrZero(message = "Score must be greater than or equal to zero")
        Double score

) {
}