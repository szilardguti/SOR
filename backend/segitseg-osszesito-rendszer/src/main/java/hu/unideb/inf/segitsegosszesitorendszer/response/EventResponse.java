package hu.unideb.inf.segitsegosszesitorendszer.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventResponse(
        UUID pub_event_id,

        LocalDateTime eventStart,

        LocalDateTime eventEnd,

        UUID debtId,

        UUID createdByUserId,

        String createdByUserName,

        List<EventAttenderResponse> attenders
) {
}
