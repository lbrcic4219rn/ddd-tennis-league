package com.github.lbrcic4219rn.dddtennisleague.presentation.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.GroupDto;
import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.LeagueDto;
import com.github.lbrcic4219rn.dddtennisleague.application.league.LeagueApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.SkillLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {
    private final LeagueApplicationService leagueService;

    public LeagueController(LeagueApplicationService leagueService) {
        this.leagueService = leagueService;
    }

    @PostMapping
    public ResponseEntity<String> createLeague(@RequestBody CreateLeagueRequest request) {
        LeagueId leagueId = leagueService.createLeague(
                request.name(),
                request.startDate(),
                request.endDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueId.value().toString());
    }

    @GetMapping("/{leagueId}")
    public ResponseEntity<LeagueDto> getLeague(@PathVariable String leagueId) {
        LeagueDto league = leagueService.getLeagueById(new LeagueId(UUID.fromString(leagueId)));
        if (league == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(league);
    }

    @GetMapping
    public ResponseEntity<List<LeagueDto>> getAllLeagues() {
        return ResponseEntity.ok(leagueService.getAllLeagues());
    }

    @DeleteMapping("/{leagueId}")
    public ResponseEntity<Void> removeLeague(@PathVariable String leagueId) {
        leagueService.removeLeague(new LeagueId(UUID.fromString(leagueId)));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{leagueId}/groups")
    public ResponseEntity<String> createGroup(
            @PathVariable String leagueId,
            @RequestBody CreateGroupRequest request) {
        String groupId = leagueService.createGroup(
                new LeagueId(UUID.fromString(leagueId)),
                SkillLevel.valueOf(request.skillLevel())
        ).value().toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(groupId);
    }

    @GetMapping("/{leagueId}/groups/{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable String leagueId, @PathVariable String groupId) {
        GroupDto group = leagueService.getGroupById(new GroupId(UUID.fromString(groupId)));
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{leagueId}/groups/{groupId}")
    public ResponseEntity<Void> removeGroup(@PathVariable String leagueId, @PathVariable String groupId) {
        leagueService.removeGroup(new GroupId(UUID.fromString(groupId)));
        return ResponseEntity.noContent().build();
    }

    public record CreateLeagueRequest(
            String name,
            Instant startDate,
            Instant endDate
    ) {
    }

    public record CreateGroupRequest(
            String skillLevel
    ) {
    }
}

