package hu.unideb.inf.segitsegosszesitorendszer.service.event;

import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddEventRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;

import java.util.List;

public interface IEventService {
    void addEvent(AddEventRequest request, String username);

    List<PubEvent> getAllCreated(String username);

    List<PubEvent> getAllAttends(String username);

    List<EventResponse> transformEventToEventResponse(List<PubEvent> events);

}
