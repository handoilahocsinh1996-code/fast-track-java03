
package com.example.day11_redis_leaderboard.adapter.in.web;

import com.example.day11_redis_leaderboard.adapter.in.web.dto.LeaderboardResponse;
import com.example.day11_redis_leaderboard.application.port.in.GetTopPlayersUseCase;
import com.example.day11_redis_leaderboard.application.port.in.UpdateScoreUseCase;
import com.example.day11_redis_leaderboard.adapter.in.web.dto.ScoreRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final UpdateScoreUseCase updateScoreUseCase;
    private final GetTopPlayersUseCase getTopPlayersUseCase;

    public LeaderboardController(
            UpdateScoreUseCase updateScoreUseCase,
            GetTopPlayersUseCase getTopPlayersUseCase
    ) {
        this.updateScoreUseCase = updateScoreUseCase;
        this.getTopPlayersUseCase = getTopPlayersUseCase;
    }

    @PostMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateScore(
            @PathVariable String username,
            @Valid @RequestBody ScoreRequest request
    ) {
        if (username.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username must not be blank"
            );
        }

        updateScoreUseCase.updateScore(username, request.score());
    }


@GetMapping("/top/{n}")
public List<LeaderboardResponse> getTopPlayers(@PathVariable int n) {
    if (n <= 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "n must be greater than zero"
        );
    }

    return getTopPlayersUseCase.getTopPlayers(n)
            .stream()
            .map(entry -> new LeaderboardResponse(
                    entry.username(),
                    entry.score(),
                    entry.rank()
            ))
            .toList();
}
}