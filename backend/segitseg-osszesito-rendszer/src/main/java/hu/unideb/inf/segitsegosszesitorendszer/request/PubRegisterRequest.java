package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PubRegisterRequest(
        @NotBlank(message = "A felhasználónév cím nem lehet üres.")
        String username,

        @NotBlank(message = "Az email cím nem lehet üres.")
        @Email(message = "Érvénytelen email cím formátum.")
        String email,

        @NotBlank(message = "A jelszó nem lehet üres.")
        String password,

        @NotBlank(message = "A hely neve nem lehet üres.")
        String name,

        @NotBlank(message = "A cím nem lehet üres.")
        String location,

        String openMonday,
        String openTuesday,
        String openWednesday,
        String openThursday,
        String openFriday,
        String openSaturday,
        String openSunday
        
) {
}
