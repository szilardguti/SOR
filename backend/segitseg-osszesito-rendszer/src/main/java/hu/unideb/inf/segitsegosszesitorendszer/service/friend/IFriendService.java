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

    List<Friend> getSentFriendRequests(String username);

    List<Friend> getReceivedFriendRequests(String username);

    List<Friend> getActiveFriends(String username);

    void updateFriend(String username, UUID friendUUID, UpdateFriendRequest request);

    public List<FriendResponse> transformFriendToFriendResponse(List<Friend> friends);
}
