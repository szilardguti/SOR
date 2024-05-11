package hu.unideb.inf.segitsegosszesitorendszer.service.item;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.repository.ItemRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;

    @Override
    public void addItem(AddItemRequest request) {
        Item newItem = Item.builder()
                .name(request.name())
                .price(request.price())
                .build();

        itemRepository.save(newItem);
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<ItemResponse> transformItemToItemResponse(List<Item> items) {
        List<ItemResponse> responses = new ArrayList<>();

        for (Item item :
                items) {
            ItemResponse response = new ItemResponse(
                    item.getItem_id(),
                    item.getName(),
                    item.getPrice()
            );
            responses.add(response);
        }
        return responses;
    }
}
