package com.github.lbrcic4219rn.dddtennisleague.domain.league.repo;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;

import java.util.Map;
import java.util.Optional;

public interface LeagueRepo {
    void save(League league);
    Optional<League> findById(LeagueId leagueId);
    void remove(LeagueId leagueId);
    Map<LeagueId, League> findAll();
    Optional<League> findByGroupId(GroupId groupId);
}
