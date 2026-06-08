package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.Membership;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.MembershipId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.MembershipRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MembershipRepoInMemory implements MembershipRepo {
    private final Map<MembershipId, Membership> memberships = new HashMap<>();

    @Override
    public void save(Membership membership) {
        memberships.put(membership.getId(), membership);
    }

    @Override
    public Optional<Membership> findById(MembershipId membershipId) {
        return Optional.ofNullable(memberships.get(membershipId));
    }

    @Override
    public void remove(MembershipId membershipId) {
        memberships.remove(membershipId);
    }

    @Override
    public List<Membership> findByGroupId(GroupId groupId) {
        return memberships.values()
                .stream()
                .filter(m -> m.getGroupId().equals(groupId))
                .toList();
    }

    @Override
    public List<Membership> findByPlayerId(PlayerId playerId) {
        return memberships.values()
                .stream()
                .filter(m -> m.getPlayerId().equals(playerId))
                .toList();
    }

    @Override
    public Optional<Membership> findByGroupIdAndPlayerId(GroupId groupId, PlayerId playerId) {
        return memberships.values()
                .stream()
                .filter(m -> m.getGroupId().equals(groupId) && m.getPlayerId().equals(playerId))
                .findFirst();
    }

    public Map<MembershipId, Membership> findAll() {
        return new HashMap<>(memberships);
    }
}

