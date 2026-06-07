package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.league.id.GroupId;
import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import com.github.lbrcic4219rn.dddtennisleague.domain.standing.id.MatchId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Match {
    private final MatchId id;
    private final GroupId groupId;
    private final PlayerId player1id;
    private final PlayerId player2id;
    private List<Set> sets;
    private MatchStatus status;

    public Match(GroupId groupId, PlayerId player1id, PlayerId player2id) {
        this.id = new MatchId(UUID.randomUUID());
        this.groupId = groupId;
        this.player1id = player1id;
        this.player2id = player2id;
        this.sets = new ArrayList<>();
        this.status = MatchStatus.SCHEDULED;
    }

    public PlayerId getWinner() {
        if (status == MatchStatus.SCHEDULED) return null;

        long player1sets = sets.stream().filter(Set::isPlayer1Winner).count();
        long player2sets = sets.stream().filter(s -> !s.isPlayer1Winner()).count();

        return player1sets > player2sets ? player1id : player2id;
    }

    public PlayerId getLoser() {
        if (status == MatchStatus.SCHEDULED) return null;
        return getWinner().equals(player1id) ? player2id : player1id;
    }

    public int getLoserSetsWon() {
        if (status == MatchStatus.SCHEDULED) throw new IllegalStateException("Cannot determine loser sets won for a match that is not completed");
        long player1sets = sets.stream().filter(Set::isPlayer1Winner).count();
        long player2sets = sets.stream().filter(s -> !s.isPlayer1Winner()).count();

        return (int) Math.min(player1sets, player2sets);
    }

    public void completeMatch(List<Set> completedSets) {
        if (completedSets.size() > 3) {
            throw new IllegalArgumentException("A match cannot have more than 3 sets");
        }

        long player1sets = completedSets.stream().filter(Set::isPlayer1Winner).count();
        long player2sets = completedSets.stream().filter(s -> !s.isPlayer1Winner()).count();

        if(Math.max(player1sets, player2sets) != 2) {
            throw new IllegalArgumentException("A match must be completed with a player winning 2 sets");
        }

        if (completedSets.size() == 3) {
            boolean firstSetWinner = completedSets.get(0).isPlayer1Winner();
            boolean secondSetWinner = completedSets.get(1).isPlayer1Winner();

            if ((firstSetWinner && secondSetWinner) || (!firstSetWinner && !secondSetWinner)) {
                throw new IllegalArgumentException("If a match is completed with 3 sets, the winner of the first two sets must be different");
            }
        }

        this.sets = completedSets;
        this.status = MatchStatus.COMPLETED;
    }
}
