package io.exterminator3618.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendListResponse extends ArrayList<UserInfo> {

}
