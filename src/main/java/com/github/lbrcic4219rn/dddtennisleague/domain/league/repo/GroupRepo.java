package com.github.lbrcic4219rn.dddtennisleague.domain.league.repo;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.Group;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;

import java.util.Optional;
import java.util.List;

public interface GroupRepo {
    void save(Group group);
    Optional<Group> findById(GroupId groupId);
    void remove(GroupId groupId);
    List<Group> findByLeagueId(LeagueId leagueId);
}

