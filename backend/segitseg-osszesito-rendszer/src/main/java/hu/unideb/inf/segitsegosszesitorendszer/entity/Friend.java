package hu.unideb.inf.segitsegosszesitorendszer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "friend_table")
public class Friend {


    @Id
    @UuidGenerator
    private UUID friend_id;

    private UUID sender;

    private UUID requested;

    private FriendStatus status;


    public enum FriendStatus {
        SENT, ACCEPTED, REMOVED
    }
}
