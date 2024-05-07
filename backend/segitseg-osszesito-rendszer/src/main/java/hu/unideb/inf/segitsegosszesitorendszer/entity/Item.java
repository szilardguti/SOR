package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_table")
public class Item {

    @Id
    @UuidGenerator
    private UUID item_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float price;

    @OneToMany(mappedBy = "stockItem")
    private Set<Stock> stocks = new HashSet<>();
}
