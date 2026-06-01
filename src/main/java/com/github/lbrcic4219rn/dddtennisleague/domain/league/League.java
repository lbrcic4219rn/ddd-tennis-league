package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class League {
    private LeagueId leagueId;
    private String name;
    private Season season;
    private List<GroupId> groups;

    public League(String name, Season season) {
        this.leagueId = new LeagueId(UUID.randomUUID());
        this.name = name;
        this.season = season;
        this.groups = new ArrayList<>();
    }

    public League(LeagueId leagueId, String name, Season season, List<GroupId> groups) {
        this.leagueId = leagueId;
        this.name = name;
        this.season = season;
        this.groups = groups;
    }

    public LeagueId getLeagueId() {
        return leagueId;
    }

    public String getName() {
        return name;
    }

    public Season getSeason() {
        return season;
    }

    public List<GroupId> getGroups() {
        return groups;
    }

    public void addGroup(GroupId groupId) {
        groups.add(groupId);
    }
}
