package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddItemRequest(
        @NotBlank(message = "A nevet kötelező megadni!")
        String name,

        @NotNull(message = "Az árat kötelező megadni!")
        Float price
) {
}
