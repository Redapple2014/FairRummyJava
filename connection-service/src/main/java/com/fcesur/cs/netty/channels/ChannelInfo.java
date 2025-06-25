package com.fcesur.cs.netty.channels;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

/**
 * Channel info
 */
@Data
public final class ChannelInfo {

    private long id;
    private Channel channel;
    private String cookieId;
    private Integer serviceId;

    public ChannelInfo(long id, @NonNull Channel channel, @NonNull Integer serviceId) {
        this.id = id;
        this.channel = channel;
        this.serviceId = serviceId;
    }
}
