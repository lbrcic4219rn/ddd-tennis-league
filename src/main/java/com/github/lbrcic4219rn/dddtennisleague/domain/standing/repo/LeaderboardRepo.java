package com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Leaderboard;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.LeaderboardId;

import java.util.List;
import java.util.Optional;

public interface LeaderboardRepo {
    void save(Leaderboard leaderboard);
    Optional<Leaderboard> findById(LeaderboardId leaderboardId);
    Optional<Leaderboard> findByGroupId(GroupId groupId);
    void remove(LeaderboardId leaderboardId);
    List<Leaderboard> findAll();
}

