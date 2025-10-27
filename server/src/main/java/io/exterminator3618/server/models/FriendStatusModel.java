package io.exterminator3618.server.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class FriendStatusModel {

    @NonNull
    public String friendName;

    @NonNull
    public String friendUsername;

    public Boolean inMatchOrOnlineStatus;

}
