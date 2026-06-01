package com.github.lbrcic4219rn.dddtennisleague.application.player.dto;

import java.time.Instant;
import java.time.LocalDate;

public record PlayerDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        Instant registeredAt
) {
}

