package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.LeagueRepo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class LeagueRepoInMemory implements LeagueRepo {
    private final Map<LeagueId, League> leagues = new HashMap<>();

    @Override
    public void save(League league) {
        leagues.put(league.getLeagueId(), league);
    }

    @Override
    public Optional<League> findById(LeagueId leagueId) {
        return Optional.ofNullable(leagues.get(leagueId));
    }

    @Override
    public void remove(LeagueId leagueId) {
        leagues.remove(leagueId);
    }

    public Map<LeagueId, League> findAll() {
        return new HashMap<>(leagues);
    }
}

