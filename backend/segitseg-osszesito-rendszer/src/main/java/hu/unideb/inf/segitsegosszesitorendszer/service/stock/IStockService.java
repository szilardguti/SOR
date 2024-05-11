package hu.unideb.inf.segitsegosszesitorendszer.service.stock;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Stock;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddOrUpdateStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.StockResponse;

import java.util.List;
import java.util.UUID;

public interface IStockService {
    void addOrUpdateStock(UUID pubUUID, AddOrUpdateStockRequest request);

    List<Stock> getStock(UUID pubUUID);

    List<StockResponse> transformStockToStockResponse(List<Stock> stocks);
}
