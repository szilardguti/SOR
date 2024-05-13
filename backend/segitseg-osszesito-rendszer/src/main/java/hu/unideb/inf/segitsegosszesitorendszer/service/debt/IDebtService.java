package hu.unideb.inf.segitsegosszesitorendszer.service.debt;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.entity.DebtItem;
import hu.unideb.inf.segitsegosszesitorendszer.exceptions.FinishedDebtException;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtItemRequest;
import hu.unideb.inf.segitsegosszesitorendszer.request.AddDebtRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtItemResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.DebtResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IDebtService {

    Debt getById(UUID id);

    List<Debt> getAllByDebtorUsername(String username);

    List<Debt> getAllByInDebtUsername(String username);

    List<DebtResponse> transformDebtToDebtResponse(List<Debt> debts);

    List<DebtItemResponse> transformDebtItemToDebtItemResponse(Set<DebtItem> debtItems);

    UUID addDebt(AddDebtRequest request, String username);

    void addOrUpdateDebtItemToDebt(UUID debtId, AddDebtItemRequest request) throws FinishedDebtException;

    void finishDebt(UUID debtId) throws FinishedDebtException;
}
