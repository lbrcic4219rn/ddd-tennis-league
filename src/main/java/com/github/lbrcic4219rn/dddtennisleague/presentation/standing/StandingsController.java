package com.github.lbrcic4219rn.dddtennisleague.presentation.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.LeaderboardDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.StandingsApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.LeaderboardId;
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
import java.util.UUID;

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
        LeaderboardDto leaderboard = standingsService.getLeaderboard(new LeaderboardId(UUID.fromString(leaderboardId)));
        if (leaderboard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<LeaderboardDto> getLeaderboardByGroup(@PathVariable String groupId) {
        LeaderboardDto leaderboard = standingsService.getLeaderboardByGroup(new GroupId(UUID.fromString(groupId)));
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
        standingsService.removeLeaderboard(new LeaderboardId(UUID.fromString(leaderboardId)));
        return ResponseEntity.noContent().build();
    }

    public record CreateLeaderboardRequest(
            String groupId
    ) {
    }
}

