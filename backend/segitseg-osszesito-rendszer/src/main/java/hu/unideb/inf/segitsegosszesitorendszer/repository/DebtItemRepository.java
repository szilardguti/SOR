package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.DebtItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DebtItemRepository extends JpaRepository<DebtItem, UUID> {
}
