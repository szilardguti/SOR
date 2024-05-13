package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "debt_items_table")
public class DebtItem {

    @Id
    @UuidGenerator
    private UUID debt_item_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debt_id")
    private Debt debt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private Integer quantity;
}