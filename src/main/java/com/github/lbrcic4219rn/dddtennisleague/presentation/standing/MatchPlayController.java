package com.github.lbrcic4219rn.dddtennisleague.presentation.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.MatchDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.MatchPlayApplicationService;
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
@RequestMapping("/api/matches")
public class MatchPlayController {
    private final MatchPlayApplicationService matchService;

    public MatchPlayController(MatchPlayApplicationService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<String> createMatch(@RequestBody CreateMatchRequest request) {
        String matchId = matchService.createMatch(
                request.groupId(),
                request.homePlayerId(),
                request.awayPlayerId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(matchId);
    }

    @PostMapping("/{matchId}/complete")
    public ResponseEntity<Void> completeMatch(
            @PathVariable String matchId,
            @RequestBody CompleteMatchRequest request) {
        matchService.completeMatch(
                matchId,
                request.winnerId(),
                request.loserId(),
                request.walkover()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDto> getMatch(@PathVariable String matchId) {
        MatchDto match = matchService.getMatchById(matchId);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(match);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MatchDto>> getGroupMatches(@PathVariable String groupId) {
        List<MatchDto> matches = matchService.getMatchesByGroup(groupId);
        return ResponseEntity.ok(matches);
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<MatchDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> removeMatch(@PathVariable String matchId) {
        matchService.removeMatch(matchId);
        return ResponseEntity.noContent().build();
    }

    public record CreateMatchRequest(
            String groupId,
            String homePlayerId,
            String awayPlayerId
    ) {
    }

    public record CompleteMatchRequest(
            String winnerId,
            String loserId,
            boolean walkover
    ) {
    }
}

