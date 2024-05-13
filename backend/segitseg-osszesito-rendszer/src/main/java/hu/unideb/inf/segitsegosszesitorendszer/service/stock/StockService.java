package hu.unideb.inf.segitsegosszesitorendszer.service.stock;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Stock;
import hu.unideb.inf.segitsegosszesitorendszer.repository.PubRepository;
import hu.unideb.inf.segitsegosszesitorendszer.repository.StockRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddOrUpdateStockRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.StockResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.item.IItemService;
import hu.unideb.inf.segitsegosszesitorendszer.service.pub.IPubService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class StockService implements IStockService{

    private final IItemService itemService;
    private final IPubService pubService;

    private final StockRepository stockRepository;
    private final PubRepository pubRepository;

    @Override
    @Transactional
    public void addOrUpdateStock(UUID pubUUID, AddOrUpdateStockRequest request) {
        Optional<Stock> stock = request.stock() == null
                ? Optional.empty()
                : stockRepository.findById(request.stock());

        if (stock.isPresent()) {
            stock.get().setQuantity(request.quantity());
            stockRepository.save(stock.get());
            return;
        }

        Pub pub = pubService.getById(pubUUID);
        Item item = itemService.getById(request.item());
        Stock newStock = Stock.builder()
                .quantity(request.quantity())
                .stockItem(item)
                .pub(pub)
                .build();
        stockRepository.save(newStock);

        pub.addStock(newStock);
        pubRepository.save(pub);
    }

    @Override
    public List<Stock> getStock(UUID pubUUID) {
        Pub pub = pubService.getById(pubUUID);
        return stockRepository.findAllByPub(pub);
    }

    @Override
    public List<StockResponse> transformStockToStockResponse(List<Stock> stocks) {
        List<StockResponse> responses = new ArrayList<>();

        for (Stock stock :
                stocks) {
            StockResponse response = new StockResponse(
                    stock.getStock_id(),
                    stock.getQuantity(),
                    stock.getStockItem().getItem_id(),
                    stock.getStockItem().getName(),
                    stock.getStockItem().getPrice()
            );
            responses.add(response);
        }
        return responses;
    }
}
