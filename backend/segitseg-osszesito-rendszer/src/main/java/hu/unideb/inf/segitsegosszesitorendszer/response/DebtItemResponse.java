package hu.unideb.inf.segitsegosszesitorendszer.response;

import java.util.UUID;

public record DebtItemResponse(
        UUID debt_item_id,

        Integer quantity,

        ItemResponse item
) {
}
