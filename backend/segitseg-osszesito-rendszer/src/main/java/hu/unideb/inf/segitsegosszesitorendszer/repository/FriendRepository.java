package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {

    boolean existsBySenderAndRequested(UUID sender, UUID requested);
}
