package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Group {
    private final GroupId id;
    private final LeagueId leagueId;
    private final SkillLevel level;
    private final List<Membership> memberships;

    public Group(GroupId id, LeagueId leagueId, SkillLevel level) {
        this.id = id;
        this.leagueId = leagueId;
        this.level = level;
        this.memberships = new ArrayList<>();
    }

    public void addMembership(Membership membership) {
        memberships.add(membership);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
    }
}
