package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.PubResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.pub.IPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/pub")
@RequiredArgsConstructor
@RestController
@Slf4j
public class PubController {
    private final IPubService pubService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all-admin")
    public ResponseEntity<List<PubResponse>> getPubsByStatus(@RequestParam(required = false) PubStatus status) {

        List<Pub> pubs = pubService.getAll(status);
        List<PubResponse> response = pubService.transformPubToPubResponse(pubs);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'PUB')")
    @GetMapping("/all")
    public ResponseEntity<List<PubResponse>> getPubs() {

        List<Pub> pubs = pubService.getAll(PubStatus.ACCEPTED);
        List<PubResponse> response = pubService.transformPubToPubResponse(pubs);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{pubUUID}")
    public ResponseEntity<String> updatePubStatus(@RequestParam PubStatus status,
                                                  @PathVariable UUID pubUUID) {

        pubService.updateStatus(pubUUID, status);
        String message = switch (status) {

            case CREATED -> "nem aktivált";
            case ACCEPTED -> "aktív";
            case BANNED -> "tiltott";
            case CLOSED -> "bezárt";
            default -> "hibás";
        };

        return ResponseEntity.ok().body(
                String.format("Új állapot sikeresen beállítva: %s", message)
        );
    }
}
