package com.github.lbrcic4219rn.dddtennisleague.domain.league;

import java.time.Instant;

public record Season(
        Instant startDate,
        Instant endDate
) {
    public Season {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}
