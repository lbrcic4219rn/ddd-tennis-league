package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

public record StandingEntryDto(
        String playerId,
        int rank,
        double points,
        int wins,
        int losses,
        int setsWon
) {
}

