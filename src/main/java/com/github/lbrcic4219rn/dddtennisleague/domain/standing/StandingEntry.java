package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;

public record StandingEntry(
        PlayerId playerId,
        int rank,
        int points,
        int wins,
        int losses,
        int setsWon,
        int setsLost
) {
    public int setsBalance() {
        return setsWon - setsLost;
    }
}
