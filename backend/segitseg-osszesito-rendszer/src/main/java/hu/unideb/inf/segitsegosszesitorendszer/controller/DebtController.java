package hu.unideb.inf.segitsegosszesitorendszer.controller;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.FinishedDebtException;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.JwtNotFoundException;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.NewJwtRequiredException;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.JwtService;
import hu.unideb.inf.segitsegosszesitorendszer.service.debt.IDebtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/debt")
@RequiredArgsConstructor
@RestController
@Slf4j
public class DebtController {

    private final IDebtService debtService;
    private final JwtService jwtService;

    // WHERE THE USER IS THE DEBTOR
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/debtor")
    public ResponseEntity<List<DebtResponse>> getDebtorDebts(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<Debt> debts = debtService.getAllByDebtorUsername(username);
        List<DebtResponse> response = debtService.transformDebtToDebtResponse(debts);

        return ResponseEntity.ok().body(response);
    }

    // WHERE THE USER IS IN DEBT
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/indebt")
    public ResponseEntity<List<DebtResponse>> getInDebtDebts(HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        List<Debt> debts = debtService.getAllByInDebtUsername(username);
        List<DebtResponse> response = debtService.transformDebtToDebtResponse(debts);

        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping()
    public ResponseEntity<UUID> addDebt(@Valid @RequestBody AddDebtRequest request,
                                        HttpServletRequest httpServletRequest)
            throws JwtNotFoundException, NewJwtRequiredException {
        String username = jwtService.getUsernameFromRequest(httpServletRequest);

        UUID newDebtId = debtService.addDebt(request, username);
        if (newDebtId == null)
            return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok().body(newDebtId);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PutMapping("/{debtId}/item")
    public ResponseEntity<String> addOrUpdateDebtItem(@Valid @RequestBody AddDebtItemRequest request,
                                                      @PathVariable UUID debtId)
            throws FinishedDebtException {

        debtService.addOrUpdateDebtItemToDebt(debtId, request);

        return ResponseEntity.ok().body("Tartozás sikeresen feljegyezve!");
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/{debtId}/finish")
    public ResponseEntity<String> addOrUpdateDebtItem(@PathVariable UUID debtId)
            throws FinishedDebtException {

        debtService.finishDebt(debtId);

        return ResponseEntity.ok().body("Tartozás sikeresen lezárva!");
    }
}
