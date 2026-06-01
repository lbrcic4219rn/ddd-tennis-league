package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private GroupId id;
    private LeagueId leagueId;
    private SkillLevel level;
    private List<Membership> memberships;

    public Group(GroupId id, LeagueId leagueId, SkillLevel level) {
        this.id = id;
        this.leagueId = leagueId;
        this.level = level;
        this.memberships = new ArrayList<>();
    }

    public GroupId getId() {
        return id;
    }

    public LeagueId getLeagueId() {
        return leagueId;
    }

    public SkillLevel getLevel() {
        return level;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void addMembership(Membership membership) {
        memberships.add(membership);
    }

    public void removeMembership(Membership membership) {
        memberships.remove(membership);
    }
}
