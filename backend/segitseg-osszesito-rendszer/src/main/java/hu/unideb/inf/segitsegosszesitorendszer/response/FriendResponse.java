package hu.unideb.inf.segitsegosszesitorendszer.response;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;

import java.util.UUID;

public record FriendResponse(

        UUID friendId,


        UUID senderId,

        String senderName,


        UUID receiverId,

        String receiverName,


        Friend.FriendStatus status
) {
}
