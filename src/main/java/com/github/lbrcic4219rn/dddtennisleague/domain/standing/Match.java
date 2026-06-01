package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match {
    private MatchId id;
    private GroupId groupId;
    private PlayerId homePlayer;
    private PlayerId awayPlayer;
    private MatchResult result;
    private List<Set> sets;

    public Match(GroupId groupId, PlayerId homePlayer, PlayerId awayPlayer) {
        this.id = new MatchId(UUID.randomUUID());
        this.groupId = groupId;
        this.homePlayer = homePlayer;
        this.awayPlayer = awayPlayer;
        this.sets = new ArrayList<>();
    }

    public Match(MatchId id, GroupId groupId, PlayerId homePlayer, PlayerId awayPlayer, MatchResult result, List<Set> sets) {
        this.id = id;
        this.groupId = groupId;
        this.homePlayer = homePlayer;
        this.awayPlayer = awayPlayer;
        this.result = result;
        this.sets = sets;
    }

    public MatchId getId() {
        return id;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public PlayerId getHomePlayer() {
        return homePlayer;
    }

    public PlayerId getAwayPlayer() {
        return awayPlayer;
    }

    public MatchResult getResult() {
        return result;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    public void addSet(Set set) {
        sets.add(set);
    }
}
