package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;

import java.time.Instant;
import java.util.UUID;

public class Membership {
    private MembershipId id;
    private GroupId groupId;
    private PlayerId playerId;
    private Instant joinedAt;
    private MembershipStatus status;

    public Membership(GroupId groupId, PlayerId playerId, MembershipStatus status) {
        this.id = new MembershipId(UUID.randomUUID());
        this.groupId = groupId;
        this.playerId = playerId;
        this.joinedAt = Instant.now();
        this.status = status;
    }

    public Membership(MembershipId id, GroupId groupId, PlayerId playerId, Instant joinedAt, MembershipStatus status) {
        this.id = id;
        this.groupId = groupId;
        this.playerId = playerId;
        this.joinedAt = joinedAt;
        this.status = status;
    }

    public MembershipId getId() {
        return id;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }
}
