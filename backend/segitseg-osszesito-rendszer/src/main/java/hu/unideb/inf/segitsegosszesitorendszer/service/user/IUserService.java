package hu.unideb.inf.segitsegosszesitorendszer.service.user;

import hu.unideb.inf.segitsegosszesitorendszer.entity.PubEvent;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.enums.Roles;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventAttenderResponse;
import hu.unideb.inf.segitsegosszesitorendszer.response.EventResponse;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    boolean userExists(String username, String email);
    User getByUsername(String username) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;

    User getById(UUID uuid);

    void addRole(UUID userUUID, Roles role);

    List<EventAttenderResponse> transformUserToEventAttenderResponse(List<User> users);
}
