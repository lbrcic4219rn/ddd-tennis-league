package com.github.lbrcic4219rn.dddtennisleague.application.league;

import com.github.lbrcic4219rn.dddtennisleague.application.league.dto.MembershipDto;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.GroupRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Membership;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.MembershipId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.LeagueRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipApplicationService {
    private final MembershipRepo membershipRepo;
    private final GroupRepo groupRepo;
    private final LeagueRepo leagueRepo;

    public String joinGroup(PlayerId playerId, GroupId groupId) throws IllegalArgumentException {
        League league = leagueRepo.findByGroupId(groupId).orElseThrow(
                () -> new IllegalArgumentException("Group not part of any league: " + groupId));

        List<Group> leagueGroups = league.getGroups();
        for (Group group : leagueGroups) {
            Optional<Membership> existing = membershipRepo.findByGroupIdAndPlayerId(group.getId(), playerId);
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Player already member of group in league: " + league.getLeagueId());
            }
        }
        Optional<Membership> existingMembership = membershipRepo.findByGroupIdAndPlayerId(groupId, playerId);
        if (existingMembership.isPresent()) {
            throw new IllegalArgumentException("Player already member of this group");
        }

        Optional<Group> group = groupRepo.findById(groupId);
        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }

        Membership membership = new Membership(groupId, playerId);
        membershipRepo.save(membership);
        group.get().addMembership(membership);
        groupRepo.save(group.get());

        return membership.getId().value().toString();
    }

    public MembershipDto getMembershipById(MembershipId membershipId) {
        Membership membership = membershipRepo.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("Membership not found: " + membershipId));
        return getMembershipDto(membership);
    }

    public List<MembershipDto> getMembersOfGroup(GroupId groupId) {
        return membershipRepo.findByGroupId(groupId)
                .stream()
                .map(this::getMembershipDto)
                .toList();
    }

    public List<MembershipDto> getPlayerMemberships(PlayerId playerId) {
        return membershipRepo.findByPlayerId(playerId)
                .stream()
                .map(this::getMembershipDto)
                .toList();
    }

    public void removeMembership(MembershipId membershipId) throws IllegalAccessException {
        membershipRepo.findById(membershipId).orElseThrow(IllegalAccessException::new);
        membershipRepo.remove(membershipId);
    }

    private MembershipDto getMembershipDto(Membership membership) {
        return new MembershipDto(
                membership.getId().value().toString(),
                membership.getGroupId().value().toString(),
                membership.getPlayerId().value().toString(),
                membership.getJoinedAt()
        );
    }
}

