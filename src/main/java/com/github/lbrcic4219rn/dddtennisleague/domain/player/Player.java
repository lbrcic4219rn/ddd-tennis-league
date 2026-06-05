package com.github.lbrcic4219rn.dddtennisleague.domain.player;

import com.github.lbrcic4219rn.dddtennisleague.domain.player.id.PlayerId;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Player {
    private final PlayerId id;
    private final String firstName;
    private final String lastName;
    private final ContactInformation contactInfo;
    private final LocalDate dateOfBirth;
    private final Instant registeredAt;

    public Player(String firstName, String lastName, ContactInformation contactInfo, LocalDate dateOfBirth) {
        validateInput(firstName, lastName, contactInfo, dateOfBirth);
        this.id = new PlayerId(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
        this.dateOfBirth = dateOfBirth;
        this.registeredAt = Instant.now();
    }

    private void validateInput(String firstName, String lastName, ContactInformation contactInfo, LocalDate dateOfBirth) {
        if (firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be blank");
        }
        if (lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be blank");
        }
        if (contactInfo == null) {
            throw new IllegalArgumentException("Contact information cannot be null");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be null or in the future");
        }
    }
}
