package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_table")
public class User {

    @Id
    @UuidGenerator
    private UUID user_id;

    @Column(unique=true, nullable = false)
    private String username;

    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birthDate;

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<PubEvent> attends = new HashSet<>();
}
