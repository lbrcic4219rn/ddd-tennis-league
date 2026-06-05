package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo.MatchRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MatchRepoInMemory implements MatchRepo {
    private final Map<MatchId, Match> matches = new HashMap<>();

    @Override
    public void save(Match match) {
        matches.put(match.getId(), match);
    }

    @Override
    public Optional<Match> findById(MatchId matchId) {
        return Optional.ofNullable(matches.get(matchId));
    }

    @Override
    public void remove(MatchId matchId) {
        matches.remove(matchId);
    }

    @Override
    public List<Match> findByGroupId(GroupId groupId) {
        return matches.values()
                .stream()
                .filter(m -> m.getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findAll() {
        return new ArrayList<>(matches.values());
    }
}

