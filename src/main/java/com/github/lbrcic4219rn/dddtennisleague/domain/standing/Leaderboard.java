package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Leaderboard {
    private LeaderboardId id;
    private GroupId groupId;
    private Instant lastUpdated;
    private List<StandingEntry> entries;

    public Leaderboard(GroupId groupId) {
        this.id = new LeaderboardId(UUID.randomUUID());
        this.groupId = groupId;
        this.lastUpdated = Instant.now();
        this.entries = new ArrayList<>();
    }

    public Leaderboard(LeaderboardId id, GroupId groupId, Instant lastUpdated, List<StandingEntry> entries) {
        this.id = id;
        this.groupId = groupId;
        this.lastUpdated = lastUpdated;
        this.entries = entries;
    }

    public LeaderboardId getId() {
        return id;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public List<StandingEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<StandingEntry> entries) {
        this.entries = entries;
        this.lastUpdated = Instant.now();
    }
}
