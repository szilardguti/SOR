package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddDebtItemRequest(

        @NotNull
        UUID itemId,

        @NotNull
        Integer quantity
) {
}
