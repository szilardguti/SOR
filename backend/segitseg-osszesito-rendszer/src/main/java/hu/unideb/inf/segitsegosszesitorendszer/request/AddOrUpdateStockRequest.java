package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddOrUpdateStockRequest(

        UUID stock,

        @NotNull(message = "A mennyiséget kötelező megadni!")
        Integer quantity,

        @NotNull(message = "Az árucikket kötelező megadni!")
        UUID item
) {
}
