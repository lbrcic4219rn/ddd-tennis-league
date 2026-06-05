package com.github.lbrcic4219rn.dddtennisleague.presentation.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.LeaderboardDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.StandingsApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/standings")
public class StandingsController {
    private final StandingsApplicationService standingsService;

    @PostMapping
    public ResponseEntity<String> createLeaderboard(@RequestBody CreateLeaderboardRequest request) {
        String leaderboardId = standingsService.createLeaderboard(request.groupId());
        return ResponseEntity.status(HttpStatus.CREATED).body(leaderboardId);
    }

    @GetMapping("/{leaderboardId}")
    public ResponseEntity<LeaderboardDto> getLeaderboard(@PathVariable String leaderboardId) {
        LeaderboardDto leaderboard = standingsService.getLeaderboard(leaderboardId);
        if (leaderboard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<LeaderboardDto> getLeaderboardByGroup(@PathVariable String groupId) {
        LeaderboardDto leaderboard = standingsService.getLeaderboardByGroup(groupId);
        if (leaderboard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardDto>> getAllLeaderboards() {
        List<LeaderboardDto> leaderboards = standingsService.getAllLeaderboards();
        return ResponseEntity.ok(leaderboards);
    }

    @DeleteMapping("/{leaderboardId}")
    public ResponseEntity<Void> removeLeaderboard(@PathVariable String leaderboardId) {
        standingsService.removeLeaderboard(leaderboardId);
        return ResponseEntity.noContent().build();
    }

    public record CreateLeaderboardRequest(
            String groupId
    ) {
    }
}

