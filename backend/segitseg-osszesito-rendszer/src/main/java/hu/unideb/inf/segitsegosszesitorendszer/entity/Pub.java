package hu.unideb.inf.segitsegosszesitorendszer.entity;

import hu.unideb.inf.segitsegosszesitorendszer.enums.PubStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pub_table")
public class Pub {

    @Id
    @UuidGenerator
    private UUID pub_id;

    @Column(unique=true, nullable = false)
    private String username;

    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private PubStatus pubStatus = PubStatus.NONE;

    private String openMonday;
    private String openTuesday;
    private String openWednesday;
    private String openThursday;
    private String openFriday;
    private String openSaturday;
    private String openSunday;

    @OneToMany(mappedBy = "pub")
    private Set<Stock> stocks = new HashSet<>();

    @OneToMany(mappedBy = "pub")
    private Set<PubEvent> events = new HashSet<>();
}
