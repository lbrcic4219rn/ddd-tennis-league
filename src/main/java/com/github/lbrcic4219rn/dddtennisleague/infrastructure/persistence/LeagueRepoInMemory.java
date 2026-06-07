package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.League;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.LeagueId;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.GroupRepo;
import com.github.lbrcic4219rn.dddtennisleague.domain.league.repo.LeagueRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Locale.filter;

@Repository
@RequiredArgsConstructor
public class LeagueRepoInMemory implements LeagueRepo {
    private final GroupRepo groupRepo;

    private final Map<LeagueId, League> leagues = new HashMap<>();

    @Override
    public void save(League league) {
        leagues.put(league.getLeagueId(), league);
    }

    @Override
    public Optional<League> findById(LeagueId leagueId) {
        leagues.get(leagueId);
        if (leagues.get(leagueId) == null) {
            return Optional.empty();
        }
        leagues.get(leagueId).getGroups().clear();
        groupRepo.findByLeagueId(leagueId).forEach(group -> leagues.get(leagueId).addGroup(group));
        return Optional.ofNullable(leagues.get(leagueId));
    }

    @Override
    public void remove(LeagueId leagueId) {
        leagues.remove(leagueId);
    }

    public Map<LeagueId, League> findAll() {
        return new HashMap<>(leagues);
    }

    @Override
    public Optional<League> findByGroupId(GroupId groupId) {
        return leagues.values().stream()
                .filter(league -> league.getGroups().stream().anyMatch(group -> group.getId().equals(groupId)))
                .findFirst();
    }

    public void removeGroup(GroupId groupId) {
        leagues.forEach((leagueId, league) -> {
            league.getGroups().removeIf(group -> group.getId().equals(groupId));
        });
    }
}

