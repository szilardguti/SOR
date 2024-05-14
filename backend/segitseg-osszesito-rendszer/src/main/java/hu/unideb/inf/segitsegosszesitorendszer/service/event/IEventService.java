package hu.unideb.inf.segitsegosszesitorendszer.service.event;

import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddEventRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;

import java.util.List;
import java.util.UUID;

public interface IEventService {

    PubEvent getById(UUID id);

    void addEvent(AddEventRequest request, String username);

    List<PubEvent> getAllCreated(String username);

    List<PubEvent> getAllAttends(String username);

    List<EventResponse> transformEventToEventResponse(List<PubEvent> events);

    void addAttender(UUID eventId, UUID userId);

    void deleteAttender(UUID eventId, UUID userId);
}
