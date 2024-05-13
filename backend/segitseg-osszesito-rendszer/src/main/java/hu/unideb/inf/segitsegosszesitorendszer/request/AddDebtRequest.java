package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddDebtRequest(

        @NotNull
        UUID debtorUserId
) {
}
