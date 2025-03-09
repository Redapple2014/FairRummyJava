package com.skillengine.rummy.player;

public class SeatPlayerInfo {
	private long userId;
	private String userName;
	private int avatarId;

	public SeatPlayerInfo(long userId, String userName, int avatarId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.avatarId = avatarId;
	}
	
		public SeatPlayerInfo(long userId) {
		super();
		this.userId = userId;
	}



	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(int avatarId) {
		this.avatarId = avatarId;
	}

}
