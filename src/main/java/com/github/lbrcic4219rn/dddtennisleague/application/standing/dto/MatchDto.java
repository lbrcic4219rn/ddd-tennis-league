package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

public record MatchDto(
        String id,
        String groupId,
        String homePlayerId,
        String awayPlayerId,
        String winnerId,
        String loserId,
        boolean walkover
) {
}

