package hu.unideb.inf.segitsegosszesitorendszer.request;

import hu.unideb.inf.segitsegosszesitorendszer.entity.Friend;
import jakarta.validation.constraints.NotNull;

public record UpdateFriendRequest(
        @NotNull
        Friend.FriendStatus friendStatus
) {
}
