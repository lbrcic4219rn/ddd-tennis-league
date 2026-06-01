package com.github.lbrcic4219rn.dddtennisleague.infrastructure.persistence;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Leaderboard;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.LeaderboardId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.LeaderboardRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LeaderboardRepoInMemory implements LeaderboardRepo {
    private final Map<LeaderboardId, Leaderboard> leaderboards = new HashMap<>();

    @Override
    public void save(Leaderboard leaderboard) {
        leaderboards.put(leaderboard.getId(), leaderboard);
    }

    @Override
    public Optional<Leaderboard> findById(LeaderboardId leaderboardId) {
        return Optional.ofNullable(leaderboards.get(leaderboardId));
    }

    @Override
    public Optional<Leaderboard> findByGroupId(GroupId groupId) {
        return leaderboards.values()
                .stream()
                .filter(l -> l.getGroupId().equals(groupId))
                .findFirst();
    }

    @Override
    public void remove(LeaderboardId leaderboardId) {
        leaderboards.remove(leaderboardId);
    }

    @Override
    public List<Leaderboard> findAll() {
        return new ArrayList<>(leaderboards.values());
    }
}

