package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

public record SetDto(
        int player1Games,
        int player2Games,
        TiebreakDto tiebreakDto
) {
}
