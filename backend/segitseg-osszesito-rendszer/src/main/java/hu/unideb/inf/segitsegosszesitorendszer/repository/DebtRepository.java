package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DebtRepository extends JpaRepository<Debt, UUID>  {

    List<Debt> findAllByDebtorUser(User debtorUser);

    List<Debt> findAllByInDebtUser(User inDebtUser);
}
