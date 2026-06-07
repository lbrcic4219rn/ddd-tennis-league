package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

public record MatchDto(
        String id,
        String groupId,
        String player1Id,
        String player2Id,
        String winnerId
) { }

