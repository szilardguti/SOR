package hu.unideb.inf.segitsegosszesitorendszer.response;

import java.util.UUID;

public record StockResponse(

        UUID stock_id,

        Integer quantity,

        UUID item_id,

        String itemName,

        Float itemPrice
) {
}
