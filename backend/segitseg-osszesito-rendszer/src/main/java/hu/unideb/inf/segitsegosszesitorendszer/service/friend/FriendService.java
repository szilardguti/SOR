package hu.unideb.inf.segitsegosszesitorendszer.service.friend;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.repository.FriendRepository;
import hu.unideb.inf.segitsegosszesitorendszer.request.UpdateFriendRequest;
import hu.unideb.inf.segitsegosszesitorendszer.response.FriendResponse;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FriendService implements IFriendService {

    private final FriendRepository friendRepository;
    private final IUserService userService;

    @Override
    public void addFriendByEmail(String senderUsername, String receiverEmail)
            throws EntityNotFoundException {

        User sender = userService.getByUsername(senderUsername);
        User receiver = userService.getByEmail(receiverEmail);

        if (friendExists(sender.getUser_id(), receiver.getUser_id()))
            return;

        Friend friendRequest = Friend.builder()
                .sender(sender.getUser_id())
                .requested(receiver.getUser_id())
                .status(Friend.FriendStatus.SENT)
                .build();

        friendRepository.save(friendRequest);
    }

    @Override
    public boolean friendExists(UUID userId1, UUID userId2) {
        return
                friendRepository.existsBySenderAndRequested(userId1, userId2)
                ||
                friendRepository.existsBySenderAndRequested(userId2, userId1);
    }

    @Override
    public Friend getById(UUID uuid) {
        Optional<Friend> friend = friendRepository.findById(uuid);

        if (friend.isEmpty())
            throw new EntityNotFoundException(
                    String.format("A barátság nem található az azonosítóval: %s", uuid)
            );
        return friend.get();
    }

    @Override
    public List<FriendResponse> getSentFriendRequests(String username) {
        User sender = userService.getByUsername(username);

        List<Friend> sentFriendRequests = friendRepository
                .findAllBySenderAndStatus(sender.getUser_id(), Friend.FriendStatus.SENT);

        return transformFriendToFriendResponse(sentFriendRequests);
    }

    @Override
    public List<FriendResponse> getReceivedFriendRequests(String username) {
        User receiver = userService.getByUsername(username);

        List<Friend> receivedFriendRequests = friendRepository
                .findAllByRequestedAndStatus(receiver.getUser_id(), Friend.FriendStatus.SENT);

        return transformFriendToFriendResponse(receivedFriendRequests);
    }

    @Override
    public List<FriendResponse> getActiveFriends(String username) {
        User user = userService.getByUsername(username);

        List<Friend> activeFriends = new ArrayList<>(friendRepository
                .findAllBySenderAndStatus(user.getUser_id(), Friend.FriendStatus.ACCEPTED));
        activeFriends.addAll(friendRepository
                .findAllByRequestedAndStatus(user.getUser_id(), Friend.FriendStatus.ACCEPTED)
        );

        return transformFriendToFriendResponse(activeFriends);
    }

    @Override
    public void updateFriend(String username, UUID friendUUID, UpdateFriendRequest request) {
        Friend friend = getById(friendUUID);
        User user = userService.getByUsername(username);

        if (!friend.getSender().equals(user.getUser_id())
                && !friend.getRequested().equals(user.getUser_id()))
            throw new AccessDeniedException("Access denied!");

        friend.setStatus(request.friendStatus());
        friendRepository.save(friend);
    }

    private List<FriendResponse> transformFriendToFriendResponse(List<Friend> friends) {
        List<FriendResponse> responses = new ArrayList<>();
        for (Friend friend :
                friends) {
            User sender = userService.getById(friend.getSender());
            User receiver = userService.getById(friend.getRequested());

            FriendResponse response = new FriendResponse(
                    friend.getFriend_id(),
                    friend.getSender(),
                    sender.getUsername(),
                    friend.getRequested(),
                    receiver.getUsername(),
                    friend.getStatus()
            );
            responses.add(response);
        }
        return responses;
    }

}
