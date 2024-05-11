package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotBlank;

public record AddFriendRequest(
        @NotBlank(message = "Az email nem lehet üres!")
        String email
) {
}
