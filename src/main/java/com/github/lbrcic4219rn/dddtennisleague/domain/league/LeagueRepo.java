package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import java.util.Map;
import java.util.Optional;

public interface LeagueRepo {
    void save(League league);
    Optional<League> findById(LeagueId leagueId);
    void remove(LeagueId leagueId);
    Map<LeagueId, League> findAll();
}
