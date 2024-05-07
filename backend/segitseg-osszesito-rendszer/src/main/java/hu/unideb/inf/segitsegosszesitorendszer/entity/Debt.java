package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "debt_table")
public class Debt {

    @Id
    @UuidGenerator
    private UUID debt_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User inDebtUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User debtorUser;

    @Column(nullable = false)
    @OneToMany(mappedBy = "debt")
    private Set<DebtItem> debtItems = new HashSet<>();

    private LocalDateTime start;

    private LocalDateTime finish;
}
