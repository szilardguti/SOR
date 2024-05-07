package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_table")
public class Friend {


    @Id
    @UuidGenerator
    private UUID debt_id;

    private UUID sender;

    private UUID requested;

    private FriendStatus status;


    private enum FriendStatus {
        SENT, ACCEPTED, REMOVED
    }
}
