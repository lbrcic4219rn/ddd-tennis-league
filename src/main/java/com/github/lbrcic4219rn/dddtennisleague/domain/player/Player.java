package com.github.lbrcic4219rn.dddtennisleague.domain.player;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Player {
    private PlayerId id;
    private String firstName;
    private String lastName;
    private ContactInformation contactInfo;
    private LocalDate dateOfBirth;
    private Instant registeredAt;

    public Player(String firstName, String lastName, ContactInformation contactInfo, LocalDate dateOfBirth) {
        this.id = new PlayerId(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
        this.dateOfBirth = dateOfBirth;
        this.registeredAt = Instant.now();
    }

    public Player(PlayerId id, String firstName, String lastName, ContactInformation contactInfo, LocalDate dateOfBirth, Instant registeredAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactInfo = contactInfo;
        this.dateOfBirth = dateOfBirth;
        this.registeredAt = registeredAt;
    }

    public PlayerId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ContactInformation getContactInfo() {
        return contactInfo;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Instant getRegisteredAt() {
        return registeredAt;
    }
}
