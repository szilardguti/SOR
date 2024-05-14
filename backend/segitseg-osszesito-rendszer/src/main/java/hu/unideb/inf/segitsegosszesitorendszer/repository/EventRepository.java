package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Debt;
import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<PubEvent, UUID> {

    List<PubEvent> findAllByCreatedByUser(UUID createdByUser);
}
