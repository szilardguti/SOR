package hu.unideb.inf.segitsegosszesitorendszer.response;

import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import jakarta.persistence.Column;

import java.util.UUID;

public record PubResponse(

        UUID pub_id,

        String email,

        String name,

        String location,

        PubStatus pubStatus,

        String openMonday,
        String openTuesday,
        String openWednesday,
        String openThursday,
        String openFriday,
        String openSaturday,
        String openSunday
) {
}
