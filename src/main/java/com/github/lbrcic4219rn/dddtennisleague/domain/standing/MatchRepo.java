package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.GroupId;

import java.util.List;
import java.util.Optional;

public interface MatchRepo {
    void save(Match match);
    Optional<Match> findById(MatchId matchId);
    void remove(MatchId matchId);
    List<Match> findByGroupId(GroupId groupId);
    List<Match> findAll();
}
