package com.github.lbrcic4219rn.dddtennisleague.application.standing.dto;

import java.time.Instant;
import java.util.List;

public record LeaderboardDto(
        String id,
        String groupId,
        Instant lastUpdated,
        List<StandingEntryDto> entries
) {
}

