package hu.unideb.inf.segitsegosszesitorendszer.service.friend;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import hu.unideb.inf.segitsegosszesitorendszer.entity.User;
import hu.unideb.inf.segitsegosszesitorendszer.repository.FriendRepository;
import hu.unideb.inf.segitsegosszesitorendszer.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


}
