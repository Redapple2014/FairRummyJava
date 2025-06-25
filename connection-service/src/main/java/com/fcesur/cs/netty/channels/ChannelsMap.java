package com.fcesur.cs.netty.channels;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Channels map
 *
 * <p>Channels map for channel id and info</p>
 */
@Slf4j
public final class ChannelsMap {

    private static final AtomicLong ID = new AtomicLong(1);
    private static final Map<Long, ChannelInfo> MAP = new ConcurrentHashMap<>();

    /**
     * Private constructor
     */
    private ChannelsMap() {
    }

    public static ChannelInfo create(@NonNull Channel channel, int serviceId) {

        // generate channel id & info
        final long id = ID.updateAndGet(value -> value == Long.MAX_VALUE ? 1 : value + 1);

        // create channel info
        final ChannelInfo info = MAP.computeIfAbsent(id, i -> new ChannelInfo(i, channel, serviceId));

        // log
        log.info("Channel created with id {}", id);

        // return
        return info;
    }

    public static void removeMapping(long channelID) {
        MAP.remove(channelID);
    }

    public static ChannelInfo getChannelInfo(long channelID) {
        return MAP.get(channelID);
    }

    public static List<ChannelInfo> getChannelInfos() {
        return new ArrayList<>(MAP.values());
    }

    public static List<Long> getChannelIds() {
        return new ArrayList<Long>(MAP.keySet());
    }
}
