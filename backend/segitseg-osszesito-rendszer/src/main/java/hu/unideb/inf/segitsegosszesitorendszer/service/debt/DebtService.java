package hu.unideb.inf.segitsegosszesitorendszer.service.debt;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.entity.DebtItem;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.FinishedDebtException;
import hu.unideb.inf.segitsegosszesitorendszer.repository.DebtItemRepository;
import hu.unideb.inf.segitsegosszesitorendszer.repository.DebtRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.friend.IFriendService;
import hu.unideb.inf.segitsegosszesitorendszer.service.item.IItemService;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DebtService implements IDebtService {

    private final IUserService userService;
    private final IFriendService friendService;
    private final IItemService itemService;

    private final DebtRepository debtRepository;
    private final DebtItemRepository debtItemRepository;

    @Override
    public Debt getById(UUID id) {
        Optional<Debt> debt = debtRepository.findById(id);

        if (debt.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A tartozás nem található az azonosítóval: %s", id)
            );
        return debt.get();
    }

    @Override
    public List<Debt> getAllByDebtorUsername(String username) {
        User debtorUser = userService.getByUsername(username);

        return debtRepository.findAllByDebtorUser(debtorUser);
    }

    @Override
    public List<Debt> getAllByInDebtUsername(String username) {
        User inDebtUser = userService.getByUsername(username);

        return debtRepository.findAllByInDebtUser(inDebtUser);
    }

    @Override
    public List<DebtResponse> transformDebtToDebtResponse(List<Debt> debts) {
        List<DebtResponse> responses = new ArrayList<>();

        for (Debt debt :
                debts) {
            double totalPrice = debt.getDebtItems()
                    .stream()
                    .mapToDouble(debtItem
                            -> debtItem.getQuantity() * debtItem.getItem().getPrice())
                    .sum();

            DebtResponse response = new DebtResponse(
                    debt.getDebt_id(),
                    debt.getInDebtUser().getUser_id(),
                    debt.getInDebtUser().getUsername(),
                    debt.getDebtorUser().getUser_id(),
                    debt.getDebtorUser().getUsername(),
                    transformDebtItemToDebtItemResponse(debt.getDebtItems()),
                    debt.getStart(),
                    debt.getFinish(),
                    (float) totalPrice

            );
            responses.add(response);
        }
        return responses;
    }

    public List<DebtItemResponse> transformDebtItemToDebtItemResponse(Set<DebtItem> debtItems) {
        List<DebtItemResponse> responses = new ArrayList<>();

        for (DebtItem debtItem :
                debtItems) {
            DebtItemResponse response = new DebtItemResponse(
                    debtItem.getDebt_item_id(),
                    debtItem.getQuantity(),
                    new ItemResponse(
                            debtItem.getItem().getItem_id(),
                            debtItem.getItem().getName(),
                            debtItem.getItem().getPrice()
                    )
            );
            responses.add(response);
        }
        return responses;
    }

    @Override
    public UUID addDebt(AddDebtRequest request, String username) {
        User debtorUser = userService.getByUsername(username);
        User inDebtUser = userService.getById(request.debtorUserId());

        if (!friendService.friendExists(debtorUser.getUser_id(), inDebtUser.getUser_id()))
            return null;

        Debt newDebt = Debt.builder()
                .inDebtUser(inDebtUser)
                .debtorUser(debtorUser)
                .start(LocalDateTime.now())
                .build();
        debtRepository.save(newDebt);

        return newDebt.getDebt_id();
    }

    @Override
    @Transactional
    public void addOrUpdateDebtItemToDebt(UUID debtId, AddDebtItemRequest request)
            throws FinishedDebtException {
        Debt debt = getById(debtId);
        if (debt.getFinish() != null)
            throw new FinishedDebtException("A tartozás már lezárásra került!");

        Item item = itemService.getById(request.itemId());

        Set<DebtItem> debtItems = debt.getDebtItems();

        Optional<DebtItem> updateDebtItem
                = debtItems
                .stream()
                .filter(debtItem -> debtItem.getItem().equals(item))
                .findFirst();

        if (updateDebtItem.isPresent()) {
            // delete item
            if (request.quantity().equals(0)) {
                debtItems.remove(updateDebtItem.get());
                debtItemRepository.delete(updateDebtItem.get());
                debtRepository.save(debt);
            }
            // update quantity
            else {
                updateDebtItem.get().setQuantity(request.quantity());
                debtItemRepository.save(updateDebtItem.get());
            }
            return;
        }

        // make new debt item
        DebtItem newDebtItem = DebtItem.builder()
                .debt(debt)
                .item(item)
                .quantity(request.quantity())
                .build();
        debtItemRepository.save(newDebtItem);

        debt.addDebtItem(newDebtItem);

        debtRepository.save(debt);
    }

    @Override
    public void finishDebt(UUID debtId) throws FinishedDebtException {
        Debt debt = getById(debtId);
        if (debt.getFinish() != null)
            throw new FinishedDebtException("A tartozás már lezárásra került!");

        debt.setFinish(LocalDateTime.now());
        debtRepository.save(debt);
    }
}
