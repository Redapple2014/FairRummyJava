package com.fcesur.cs.netty.channels;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Channel id generator
 */
@Deprecated
public final class ChannelIdGenerator {

    private static final AtomicLong channelID = new AtomicLong(1);

    /**
     * Private constructor
     */
    private ChannelIdGenerator() {
    }

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
