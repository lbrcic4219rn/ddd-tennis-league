package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;

import java.util.List;
import java.util.Optional;

public interface MembershipRepo {
    void save(Membership membership);
    Optional<Membership> findById(MembershipId membershipId);
    void remove(MembershipId membershipId);
    List<Membership> findByGroupId(GroupId groupId);
    List<Membership> findByPlayerId(PlayerId playerId);
    Optional<Membership> findByGroupIdAndPlayerId(GroupId groupId, PlayerId playerId);
}

