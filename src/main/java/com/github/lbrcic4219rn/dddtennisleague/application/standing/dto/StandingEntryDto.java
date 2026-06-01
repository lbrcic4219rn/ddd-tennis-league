package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

public record StandingEntryDto(
        String playerId,
        int rank,
        int points,
        int wins,
        int losses,
        int setsWon,
        int setsLost
) {
}

