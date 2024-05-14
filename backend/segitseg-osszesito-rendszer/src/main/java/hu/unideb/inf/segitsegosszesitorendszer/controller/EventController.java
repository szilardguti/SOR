package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.NewJwtRequiredException;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddEventRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.event.IEventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/event")
@RequiredArgsConstructor
@RestController
@Slf4j
public class EventController {

    private final IEventService eventService;
    private final JwtService jwtService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<String> addEvent(@Valid @RequestBody AddEventRequest request,
                                           HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        eventService.addEvent(request, username);

        return ResponseEntity.ok().body("Esemény sikeresen módosítva!");
    }


    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/created")
    public ResponseEntity<List<EventResponse>> getCreatedEvents(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<PubEvent> events = eventService.getAllCreated(username);
        List<EventResponse> responses = eventService.transformEventToEventResponse(events);

        return ResponseEntity.ok().body(responses);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/attends")
    public ResponseEntity<List<EventResponse>> getAttendEvents(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<PubEvent> events = eventService.getAllAttends(username);
        List<EventResponse> responses = eventService.transformEventToEventResponse(events);

        return ResponseEntity.ok().body(responses);
    }
}
