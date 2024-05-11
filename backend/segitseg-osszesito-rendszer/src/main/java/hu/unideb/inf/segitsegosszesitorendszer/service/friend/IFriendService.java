package hu.unideb.inf.segitsegosszesitorendszer.service.friend;

import java.util.UUID;

public interface IFriendService {
    void addFriendByEmail(String username, String email);

    boolean friendExists(UUID userId1, UUID userId2);
}
