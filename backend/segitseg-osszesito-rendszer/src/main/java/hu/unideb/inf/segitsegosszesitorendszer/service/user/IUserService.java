package hu.unideb.inf.segitsegosszesitorendszer.service.user;

import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

public interface IUserService {
    User getByUsername(String username) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;

    User getById(UUID uuid);
}
