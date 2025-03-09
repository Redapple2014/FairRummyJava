package com.connection.netty.channels;

import java.util.concurrent.atomic.AtomicLong;

public class ChannelIdGenerator {
	static AtomicLong channelID = new AtomicLong(1);

	public static void init() {
		try {
			if (channelID.get() <= 0) {
				channelID.set(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static long getNextChannelID() {
		try {
			return ChannelIdGenerator.channelID.getAndIncrement();
		} catch (Exception e) {
			e.printStackTrace();
			return 0l;
		}
	}
}
