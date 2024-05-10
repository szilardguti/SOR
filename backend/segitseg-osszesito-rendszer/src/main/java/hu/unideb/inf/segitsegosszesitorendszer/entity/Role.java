package hu.unideb.inf.segitsegosszesitorendszer.entity;

import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import jakarta.persistence.Column;
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
@Table(name = "role_table")
public class Role {

    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(unique=true, nullable = false)
    private Roles role;

}
