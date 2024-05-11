package hu.unideb.inf.segitsegosszesitorendszer.service.friend;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import hu.unideb.inf.segitsegosszesitorendszer.request.UpdateFriendRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.FriendResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IFriendService {
    void addFriendByEmail(String username, String email);

    boolean friendExists(UUID userId1, UUID userId2);

    Friend getById(UUID uuid);

    List<FriendResponse> getSentFriendRequests(String username);

    List<FriendResponse> getReceivedFriendRequests(String username);

    List<FriendResponse> getActiveFriends(String username);

    void updateFriend(String username, UUID friendUUID, UpdateFriendRequest request);
}
