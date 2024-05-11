package hu.unideb.inf.segitsegosszesitorendszer.service.item;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;

import java.util.List;
import java.util.UUID;

public interface IItemService {
    void addItem(AddItemRequest request);

    List<Item> getAll();

    List<ItemResponse> transformItemToItemResponse(List<Item> items);

    Item getById(UUID id);
}
