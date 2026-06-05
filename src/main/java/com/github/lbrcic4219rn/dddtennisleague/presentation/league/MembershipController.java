package com.github.lbrcic4219rn.dddtennisleague.presentation.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.MembershipApplicationService;
import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.MembershipDto;
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
@RequestMapping("/api/memberships")
public class MembershipController {
    private final MembershipApplicationService membershipService;

    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestBody JoinGroupRequest request) {
        String membershipId = membershipService.joinGroup(request.playerId(), request.groupId());
        return ResponseEntity.status(HttpStatus.CREATED).body(membershipId);
    }

    @PostMapping("/leave/{membershipId}")
    public ResponseEntity<Void> leaveGroup(@PathVariable String membershipId) {
        membershipService.leaveGroup(membershipId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{membershipId}")
    public ResponseEntity<MembershipDto> getMembership(@PathVariable String membershipId) {
        MembershipDto membership = membershipService.getMembershipById(membershipId);
        if (membership == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(membership);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MembershipDto>> getGroupMembers(@PathVariable String groupId) {
        List<MembershipDto> members = membershipService.getMembersOfGroup(groupId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<MembershipDto>> getPlayerMemberships(@PathVariable String playerId) {
        List<MembershipDto> memberships = membershipService.getPlayerMemberships(playerId);
        return ResponseEntity.ok(memberships);
    }

    @DeleteMapping("/{membershipId}")
    public ResponseEntity<Void> removeMembership(@PathVariable String membershipId) {
        membershipService.removeMembership(membershipId);
        return ResponseEntity.noContent().build();
    }

    public record JoinGroupRequest(
            String playerId,
            String groupId
    ) {
    }
}

