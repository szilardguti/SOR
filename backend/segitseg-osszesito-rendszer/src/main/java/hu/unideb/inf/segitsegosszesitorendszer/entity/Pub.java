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

    @Column(unique=true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

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
