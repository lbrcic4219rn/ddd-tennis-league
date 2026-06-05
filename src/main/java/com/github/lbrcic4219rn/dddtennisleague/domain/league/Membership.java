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

    private MembershipStatus status;

    public Membership(GroupId groupId, PlayerId playerId, MembershipStatus status) {
        this.id = new MembershipId(UUID.randomUUID());
        this.groupId = groupId;
        this.playerId = playerId;
        this.joinedAt = Instant.now();
        this.status = status;
    }

    public void activateMembership() {
        if (status.equals(MembershipStatus.ACTIVE)) {
            throw new IllegalStateException("Membership is already active.");
        }
        this.status = MembershipStatus.ACTIVE;
    }

    public void deactivateMembership() {
        if (status.equals(MembershipStatus.INACTIVE)) {
            throw new IllegalStateException("Membership is already inactive.");
        }
        this.status = MembershipStatus.INACTIVE;
    }
}
