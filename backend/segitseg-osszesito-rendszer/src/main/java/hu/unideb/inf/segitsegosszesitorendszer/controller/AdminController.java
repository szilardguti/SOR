package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminController {

    private final IUserService userService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/{userUUID}")
    public ResponseEntity<String> addAdminRole(@PathVariable UUID userUUID) {
        userService.addRole(userUUID, Roles.ADMIN);

        return ResponseEntity.ok().body("Admin jog sikeresen hozzárendelve!");
    }
}
