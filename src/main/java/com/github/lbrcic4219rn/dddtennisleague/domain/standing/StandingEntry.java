package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;

public record StandingEntry(
        PlayerId playerId,
        double points,
        int wins,
        int losses,
        int setsWon
) {
    public StandingEntry {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (wins < 0) {
            throw new IllegalArgumentException("Wins cannot be negative");
        }
        if (losses < 0) {
            throw new IllegalArgumentException("Losses cannot be negative");
        }
        if (setsWon < 0) {
            throw new IllegalArgumentException("Sets won cannot be negative");
        }
    }
}
