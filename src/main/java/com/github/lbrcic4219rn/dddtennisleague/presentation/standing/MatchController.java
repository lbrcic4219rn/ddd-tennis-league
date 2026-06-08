package com.github.lbrcic4219rn.dddtennisleague.presentation.standing;

import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.MatchDto;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.MatchApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.standing.dto.SetDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;
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
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchApplicationService matchService;

    @PostMapping
    public ResponseEntity<String> createMatch(@RequestBody CreateMatchRequest request) {
        try {
            String matchId = matchService.createMatch(
                    new GroupId(UUID.fromString(request.groupId())),
                    new PlayerId(UUID.fromString(request.player1Id())),
                    new PlayerId(UUID.fromString(request.player2Id()))
            ).value().toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(matchId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/{matchId}/complete")
    public ResponseEntity<String> completeMatch(
            @PathVariable String matchId,
            @RequestBody CompleteMatchRequest request) {
        try {
            matchService.completeMatch(
                    new MatchId(UUID.fromString(matchId)),
                    request.sets()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDto> getMatch(@PathVariable String matchId) {
        try {
            MatchDto match = matchService.getMatchById(new MatchId(UUID.fromString(matchId)));
            return ResponseEntity.ok(match);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MatchDto>> getGroupMatches(@PathVariable String groupId) {
        List<MatchDto> matches = matchService.getMatchesByGroup(new GroupId(UUID.fromString(groupId)));
        return ResponseEntity.ok(matches);
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<MatchDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> removeMatch(@PathVariable String matchId) {
        try {
            matchService.removeMatch(new MatchId(UUID.fromString(matchId)));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public record CreateMatchRequest(
            String groupId,
            String player1Id,
            String player2Id
    ) {
    }

    public record CompleteMatchRequest(
             List<SetDto> sets
    ) {
    }
}

