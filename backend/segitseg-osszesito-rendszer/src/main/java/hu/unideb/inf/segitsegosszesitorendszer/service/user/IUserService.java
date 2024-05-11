package hu.unideb.inf.segitsegosszesitorendszer.service.user;

import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public interface IUserService {

    boolean userExists(String username, String email);
    User getByUsername(String username) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;

    User getById(UUID uuid);

    void addRole(UUID userUUID, Roles role);
}
