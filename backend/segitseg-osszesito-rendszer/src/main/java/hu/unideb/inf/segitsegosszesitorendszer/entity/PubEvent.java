package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Builder
@Table(name = "pub_event_table")
public class PubEvent {

    @Id
    @UuidGenerator
    private UUID pub_event_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Pub pub;

    @Column(nullable = false)
    private LocalDateTime eventStart;

    private LocalDateTime eventEnd; // simple 'end' made jdbc errors lol

    @Column(nullable = true)
    private UUID debtId;

    @Column(nullable = false)
    private UUID createdByUser;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> registeredUsers = new HashSet<>();

    public boolean addAttendingUser(User user) {
        return this.registeredUsers.add(user);
    }
}
