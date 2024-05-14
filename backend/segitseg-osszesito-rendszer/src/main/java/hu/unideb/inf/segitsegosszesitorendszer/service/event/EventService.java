package hu.unideb.inf.segitsegosszesitorendszer.service.event;

import hu.unideb.inf.segitsegosszesitorendszer.entity.*;
import hu.unideb.inf.segitsegosszesitorendszer.repository.EventRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddEventRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.debt.IDebtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.pub.IPubService;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EventService implements IEventService {

    private final IPubService pubService;
    private final IDebtService debtService;
    private final IUserService userService;

    private final EventRepository eventRepository;

    @Override
    public void addEvent(AddEventRequest request, String username) {
        User creatorUser = userService.getByUsername(username);
        Pub pub = pubService.getById(request.pubId());

        PubEvent newPubEvent = PubEvent.builder()
                .pub(pub)
                .debtId(request.debtId())
                .eventStart(request.start())
                .createdByUser(creatorUser.getUser_id())
                .registeredUsers(new HashSet<>())
                .build();

        if (request.debtId() != null) {
            Debt debt = debtService.getById(request.debtId());
            newPubEvent.addAttendingUser(debt.getDebtorUser());
            newPubEvent.addAttendingUser(debt.getInDebtUser());
        }

        eventRepository.save(newPubEvent);
    }

    @Override
    public List<PubEvent> getAllCreated(String username) {
        User user = userService.getByUsername(username);

        return eventRepository.findAllByCreatedByUser(user.getUser_id());
    }

    @Override
    public List<PubEvent> getAllAttends(String username) {
        User user = userService.getByUsername(username);

        return eventRepository.findAll().stream()
                .filter(pubEvent
                        -> pubEvent
                        .getRegisteredUsers()
                        .contains(user)
                ).toList();
    }

    @Override
    public List<EventResponse> transformEventToEventResponse(List<PubEvent> events) {
        List<EventResponse> responses = new ArrayList<>();

        for (PubEvent event :
                events) {
            User creator = userService.getById(event.getCreatedByUser());

            EventResponse response = new EventResponse(
                    event.getPub_event_id(),
                    event.getEventStart(),
                    event.getEventEnd(),
                    event.getDebtId(),
                    creator.getUser_id(),
                    creator.getUsername(),
                    userService.transformUserToEventAttenderResponse(
                            event.getRegisteredUsers().stream().toList()
                    )
            );
            responses.add(response);
        }
        return responses;
    }
}
