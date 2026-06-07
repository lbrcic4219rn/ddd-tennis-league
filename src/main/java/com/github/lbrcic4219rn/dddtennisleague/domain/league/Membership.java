package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.MembershipId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Membership {
    private final MembershipId id;
    private final GroupId groupId;
    private final PlayerId playerId;
    private final Instant joinedAt;

    public Membership(GroupId groupId, PlayerId playerId) {
        this.id = new MembershipId(UUID.randomUUID());
        this.groupId = groupId;
        this.playerId = playerId;
        this.joinedAt = Instant.now();
    }
}
