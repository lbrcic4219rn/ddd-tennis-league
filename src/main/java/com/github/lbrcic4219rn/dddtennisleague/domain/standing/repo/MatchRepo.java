package com.github.lbrcic4219rn.dddtennisleague.domain.standing.repo;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.Match;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;

import java.util.List;
import java.util.Optional;

public interface MatchRepo {
    void save(Match match);
    Optional<Match> findById(MatchId matchId);
    void remove(MatchId matchId);
    List<Match> findByGroupId(GroupId groupId);
    List<Match> findAll();
}
