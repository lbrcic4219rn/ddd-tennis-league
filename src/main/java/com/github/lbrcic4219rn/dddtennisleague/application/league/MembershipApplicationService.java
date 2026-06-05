package com.github.lbrcic4219rn.dddtennisleague.application.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.MembershipDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.GroupRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Membership;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.MembershipId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.MembershipStatus;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MembershipApplicationService {
    private final MembershipRepo membershipRepo;
    private final GroupRepo groupRepo;

    public MembershipApplicationService(MembershipRepo membershipRepo, GroupRepo groupRepo) {
        this.membershipRepo = membershipRepo;
        this.groupRepo = groupRepo;
    }

    public String joinGroup(String playerId, String groupId) {
        PlayerId playerIdObj = new PlayerId(UUID.fromString(playerId));
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));

        // Check for existing membership (invariant: exactly 1 group per player per league)
        Optional<Membership> existingMembership = membershipRepo.findByGroupIdAndPlayerId(groupIdObj, playerIdObj);
        if (existingMembership.isPresent()) {
            throw new IllegalArgumentException("Player already member of this group");
        }

        Optional<Group> group = groupRepo.findById(groupIdObj);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }

        Membership membership = new Membership(groupIdObj, playerIdObj, MembershipStatus.ACTIVE);
        membershipRepo.save(membership);
        group.get().addMembership(membership);
        groupRepo.save(group.get());

        return membership.getId().value().toString();
    }

    public void leaveGroup(String membershipId) {
        MembershipId membershipIdObj = new MembershipId(UUID.fromString(membershipId));
        Optional<Membership> membership = membershipRepo.findById(membershipIdObj);

        if (membership.isEmpty()) {
            throw new IllegalArgumentException("Membership not found: " + membershipId);
        }

        GroupId groupId = membership.get().getGroupId();
        Optional<Group> group = groupRepo.findById(groupId);
        if (group.isPresent()) {
            group.get().removeMembership(membership.get());
            groupRepo.save(group.get());
        }

        membershipRepo.remove(membershipIdObj);
    }

    public MembershipDto getMembershipById(String membershipId) {
        MembershipId membershipIdObj = new MembershipId(UUID.fromString(membershipId));
        Optional<Membership> membership = membershipRepo.findById(membershipIdObj);
        return membership.map(this::convertToDto).orElse(null);
    }

    public List<MembershipDto> getMembersOfGroup(String groupId) {
        GroupId groupIdObj = new GroupId(UUID.fromString(groupId));
        return membershipRepo.findByGroupId(groupIdObj)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MembershipDto> getPlayerMemberships(String playerId) {
        PlayerId playerIdObj = new PlayerId(UUID.fromString(playerId));
        return membershipRepo.findByPlayerId(playerIdObj)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void removeMembership(String membershipId) {
        MembershipId membershipIdObj = new MembershipId(UUID.fromString(membershipId));
        membershipRepo.remove(membershipIdObj);
    }

    private MembershipDto convertToDto(Membership membership) {
        return new MembershipDto(
                membership.getId().value().toString(),
                membership.getGroupId().value().toString(),
                membership.getPlayerId().value().toString(),
                membership.getJoinedAt(),
                membership.getStatus().toString()
        );
    }
}

