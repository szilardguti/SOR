package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PubRepository extends JpaRepository<Pub, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Pub> findByUsername(String username);
}
