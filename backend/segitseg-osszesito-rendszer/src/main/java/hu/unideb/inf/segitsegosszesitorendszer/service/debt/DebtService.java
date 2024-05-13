package hu.unideb.inf.segitsegosszesitorendszer.service.debt;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.entity.DebtItem;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Item;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.repository.DebtRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.ItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.friend.IFriendService;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DebtService implements IDebtService {

    private final IUserService userService;
    private final IFriendService friendService;
    private final DebtRepository debtRepository;

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
}
