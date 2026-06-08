package com.github.lbrcic4219rn.dddtennisleague.presentation.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.MembershipApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.MembershipDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.MembershipId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
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
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipApplicationService membershipService;

    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody JoinGroupRequest request) {
        try {
            String membershipId = membershipService.joinGroup(
                    new PlayerId(UUID.fromString(request.playerId())),
                    new GroupId(UUID.fromString(request.groupId())));
            return ResponseEntity.status(HttpStatus.CREATED).body(membershipId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipDto> getMembership(@PathVariable String membershipId) {
        MembershipDto membership = membershipService.getMembershipById(new MembershipId(UUID.fromString(membershipId)));
        if (membership == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(membership);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MembershipDto>> getGroupMembers(@PathVariable String groupId) {
        List<MembershipDto> members = membershipService.getMembersOfGroup(new GroupId(UUID.fromString(groupId)));
        return ResponseEntity.ok(members);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<MembershipDto>> getPlayerMemberships(@PathVariable String playerId) {
        List<MembershipDto> memberships = membershipService.getPlayerMemberships(new PlayerId(UUID.fromString(playerId)));
        return ResponseEntity.ok(memberships);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<Void> removeMembership(@PathVariable String membershipId) {
        try {
            membershipService.removeMembership(new MembershipId(UUID.fromString(membershipId)));
        } catch (IllegalAccessException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    public record JoinGroupRequest(
            String playerId,
            String groupId
    ) {
    }
}

