package com.github.lbrcic4219rn.dddtennisleague.application.league.dto;

public record GroupDto(
        String id,
        String leagueId,
        String skillLevel,
        int memberCount
) {
}

