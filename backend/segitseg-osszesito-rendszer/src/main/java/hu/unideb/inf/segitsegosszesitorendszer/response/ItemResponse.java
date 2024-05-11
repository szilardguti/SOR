package hu.unideb.inf.segitsegosszesitorendszer.response;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ItemResponse(

        UUID id,

        String name,

        Float price
) {
}
