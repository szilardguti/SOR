package hu.unideb.inf.segitsegosszesitorendszer.service.user;

import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import jakarta.persistence.EntityNotFoundException;

public interface IUserService {
    User getByUsername(String username) throws EntityNotFoundException;

    User getByEmail(String email) throws EntityNotFoundException;
}
