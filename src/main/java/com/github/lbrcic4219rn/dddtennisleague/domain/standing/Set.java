package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

public record Set(
        int setNumber,
        int homeGames,
        int awayGames,
        TieBreak tieBreak
) {
}
