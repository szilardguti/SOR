package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record SearchPubStockRequest(
        @NotNull
        @NotEmpty
        Set<UUID> items
) {
}
