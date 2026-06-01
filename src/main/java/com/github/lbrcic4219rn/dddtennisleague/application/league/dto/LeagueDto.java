package com.github.lbrcic4219rn.dddtennisleague.application.league.dto;

import java.time.Instant;
import java.util.List;

public record LeagueDto(
        String id,
        String name,
        Instant startDate,
        Instant endDate,
        List<GroupDto> groups
) {
}

