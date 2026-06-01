package com.github.lbrcic4219rn.dddtennisleague.domain.standing;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.PlayerId;

public record MatchResult(
        PlayerId winner,
        PlayerId loser,
        boolean walkover
) {
}
