package com.github.lbrcic4219rn.dddtennisleague.application.league.dto;

import java.time.Instant;

public record MembershipDto(
        String id,
        String groupId,
        String playerId,
        Instant joinedAt,
        String status
) {
}

