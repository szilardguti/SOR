package hu.unideb.inf.segitsegosszesitorendszer.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record AddEventRequest(

        @NotNull
        UUID pubId,

        UUID debtId,

        @NotNull
        LocalDateTime start
        )
{}
