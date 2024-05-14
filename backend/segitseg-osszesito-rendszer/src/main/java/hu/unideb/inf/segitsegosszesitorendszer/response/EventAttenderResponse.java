package hu.unideb.inf.segitsegosszesitorendszer.response;

import java.util.UUID;

public record EventAttenderResponse(
        UUID user_id,

        String username,

        String email
) {
}
