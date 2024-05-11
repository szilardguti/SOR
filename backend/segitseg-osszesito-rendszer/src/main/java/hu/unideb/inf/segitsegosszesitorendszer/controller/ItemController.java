package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.item.IItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/item")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ItemController {

    private final IItemService itemService;


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<String> addItem(@Valid @RequestBody AddItemRequest request) {
        itemService.addItem(request);

        return ResponseEntity.ok().body("Árucikk sikeresen hozzáadva!");
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'PUB')")
    @GetMapping("/all")
    public ResponseEntity<List<ItemResponse>> getItems() {

        List<Item> items = itemService.getAll();
        List<ItemResponse> response = itemService.transformItemToItemResponse(items);

        return ResponseEntity.ok().body(response);
    }
}
