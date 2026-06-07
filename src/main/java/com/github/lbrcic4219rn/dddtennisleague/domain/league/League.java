package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class League {
    private final LeagueId leagueId;
    private final String name;
    private final Season season;
    private final List<Group> groups;

    public League(String name, Season season) {
        this.leagueId = new LeagueId(UUID.randomUUID());
        this.name = name;
        this.season = season;
        this.groups = new ArrayList<>();
    }

    public void addGroup(Group group) {
        groups.add(group);
    }
}
