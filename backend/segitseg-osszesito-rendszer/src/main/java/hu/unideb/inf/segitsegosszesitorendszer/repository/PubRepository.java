package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PubRepository extends JpaRepository<Pub, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Pub> findByUsername(String username);

    List<Pub> findAllByPubStatus(PubStatus status);
}
