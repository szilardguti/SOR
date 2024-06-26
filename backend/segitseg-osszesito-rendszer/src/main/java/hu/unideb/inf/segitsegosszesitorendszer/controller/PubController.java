package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Stock;
import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddOrUpdateStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.SearchPubStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.PubResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.StockResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.pub.IPubService;
import hu.unideb.inf.segitsegosszesitorendszer.service.stock.IStockService;
import jakarta.validation.Valid;
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
    private final IStockService stockService;

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

    // IF NO STOCK ID IS GIVEN A NEW STOCK IS ADDED!
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PUB')")
    @PutMapping("/{pubUUID}/stock")
    public ResponseEntity<String> addOrUpdateStock(@Valid @RequestBody AddOrUpdateStockRequest request,
                                                   @PathVariable UUID pubUUID) {

        stockService.addOrUpdateStock(pubUUID, request);

        return ResponseEntity.ok().body("Raktári elem sikeresen módosítva!");
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PUB')")
    @GetMapping("/{pubUUID}/stock")
    public ResponseEntity<List<StockResponse>> getStock(@PathVariable UUID pubUUID) {

        List<Stock> stocks = stockService.getStock(pubUUID);
        List<StockResponse> response = stockService.transformStockToStockResponse(stocks);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/stock/search")
    public ResponseEntity<List<PubResponse>> searchStock(@Valid @RequestBody SearchPubStockRequest request) {

        List<Pub> pubs = pubService.getAllByStock(request);
        List<PubResponse> response = pubService.transformPubToPubResponse(pubs);

        return ResponseEntity.ok().body(response);
    }
}
