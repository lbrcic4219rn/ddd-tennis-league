package com.github.lbrcic4219rn.dddtennisleague.domain.player;

public record ContactInformation (
        String email,
        String phoneNumber
) {
    public static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String phoneNumberRegex = "^\\+?[0-9. ()-]{7,25}$";

    public ContactInformation {
        if (email == null || email.isBlank() || !email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (phoneNumber == null || phoneNumber.isBlank() || !phoneNumber.matches(phoneNumberRegex)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
