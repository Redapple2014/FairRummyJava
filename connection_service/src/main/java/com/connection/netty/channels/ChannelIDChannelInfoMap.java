package com.connection.netty.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelIDChannelInfoMap {
	private static final Map<Long, ChannelInfo> channelIDvsChannelMap = new ConcurrentHashMap<>();

	public static ChannelInfo createChannelIDAndAddInfoToMap(Channel channel ,int serviceId) {
		long channelID = ChannelIdGenerator.getNextChannelID();
		ChannelInfo channelInfo = new ChannelInfo(channelID, channel,serviceId);
		ChannelInfo channelInfo1 = channelIDvsChannelMap.put(channelID, channelInfo);
		if (channelInfo1 != null) {
			System.err.println("Duplicate Channel ID generated for Channel - " + channel.remoteAddress());
		}
		return channelInfo;
	}

	public static void removeMapping(long channelID) {
		channelIDvsChannelMap.remove(channelID);
	}

	public static ChannelInfo getChannelInfo(long channelID) {
		return channelIDvsChannelMap.get(channelID);
	}

	public static List<ChannelInfo> getChannelInfos() {
		return new ArrayList<ChannelInfo>(channelIDvsChannelMap.values());
	}

	public static List<Long> getChannelIds() {
		return new ArrayList<Long>(channelIDvsChannelMap.keySet());
	}
}
