package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.LeaderboardId;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
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

    public void setEntries(List<StandingEntry> entries) {
        this.entries = entries;
        this.lastUpdated = Instant.now();
    }
}
