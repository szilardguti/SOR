package hu.unideb.inf.segitsegosszesitorendszer.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record DebtResponse(

        UUID debt_id,

        UUID inDebtUserId,

        String inDebtUserName,

        UUID debtorUserId,

        String debtorUserName,

        List<DebtItemResponse> debtItems,

        LocalDateTime start,

        LocalDateTime finish,

        Float totalPrice
) {
}
