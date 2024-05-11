package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddFriendRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.UpdateFriendRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.FriendResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.friend.IFriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/user/friend")
@RequiredArgsConstructor
@RestController
@Slf4j
public class FriendController {

    private final IFriendService friendService;
    private final JwtService jwtService;

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping()
    public ResponseEntity<String> addFriendByEmail(@Valid @RequestBody AddFriendRequest request,
                                                   HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        friendService.addFriendByEmail(username, request.email());

        return ResponseEntity.ok().body("Barát felkérés elküldve!");
    }

    // SENT BY THE USER AND NOT ACCEPTED
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/sent")
    public ResponseEntity<List<FriendResponse>> getSentFriendRequests(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<FriendResponse> response = friendService.getSentFriendRequests(username);

        return ResponseEntity.ok().body(response);
    }

    // SENT BY SOMEONE ELSE FOR THE USER AND NOT ACCEPTED
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/received")
    public ResponseEntity<List<FriendResponse>> getReceivedFriendRequests(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<FriendResponse> response = friendService.getReceivedFriendRequests(username);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/active")
    public ResponseEntity<List<FriendResponse>> getActiveFriends(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<FriendResponse> response = friendService.getActiveFriends(username);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/{friendUUID}")
    public ResponseEntity<String> updateFriendRequest(@PathVariable UUID friendUUID,
                                                                    @RequestBody @Valid UpdateFriendRequest request,
                                                                    HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        if (request.friendStatus() != Friend.FriendStatus.ACCEPTED &&
                request.friendStatus() != Friend.FriendStatus.REMOVED)
            return ResponseEntity.badRequest().body(null);

        friendService.updateFriend(username, friendUUID, request);

        String message = switch (request.friendStatus())
                {
                    case ACCEPTED -> "Kérés sikeresen elfogadva!";
                    case REMOVED -> "Sikeres letiltás!";
                    default -> "Hibás kérelem!";
                };

        return ResponseEntity.ok().body(message);
    }

}
