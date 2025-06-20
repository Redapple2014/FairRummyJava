package com.skillengine.rummy.message;

import static com.skillengine.rummy.message.MessageConstants.USER_INFO;

public class UserInfo extends Message {
	private long userId;
	private String userName;
	private int avatarId;

	public long getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public int getAvatarId() {
		return avatarId;
	}

	public UserInfo(long tableId, long userId, String name, int avatarId) {
		super(1, USER_INFO, tableId);
		this.userId = userId;
		this.userName = name;
		this.avatarId = avatarId;
	}

}
