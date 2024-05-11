package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddFriendRequest;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.friend.IFriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user/friend")
@RequiredArgsConstructor
@RestController
@Slf4j
public class FriendController {

    private final IFriendService friendService;
    private final JwtService jwtService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<String> addFriendByEmail(@Valid @RequestBody AddFriendRequest request,
                                                   HttpServletRequest httpServletRequest)
            throws JwtNotFoundException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        friendService.addFriendByEmail(username, request.email());

        return ResponseEntity.ok().body("Barát felkérés elküldve!");
    }

}
