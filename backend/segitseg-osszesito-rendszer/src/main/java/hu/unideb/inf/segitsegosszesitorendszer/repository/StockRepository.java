package hu.unideb.inf.segitsegosszesitorendszer.repository;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Pub;
import hu.unideb.inf.segitsegosszesitorendszer.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    List<Stock> findAllByPub(Pub pub);
}
