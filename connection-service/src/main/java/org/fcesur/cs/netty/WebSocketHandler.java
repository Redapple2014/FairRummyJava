package org.fcesur.cs.netty;

import org.fcesur.cs.cookie.CookieVsUserID;
import org.fcesur.cs.jackson.JacksonObjectWrapper;
import org.fcesur.cs.msg.Frame;
import org.fcesur.cs.msg.MessageConstants;
import org.fcesur.cs.msg.MessageDigester;
import org.fcesur.cs.msg.MessageParser;
import org.fcesur.cs.netty.channels.ChannelInfo;
import org.fcesur.cs.netty.channels.ChannelsMap;
import org.fcesur.cs.netty.channels.UserIDVsChannelInfo;
import org.fcesur.cs.services.PlayerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Web socket handler
 */
@Slf4j
public final class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    private static final int CS_SERVICE_ID = 1;
    private static final AttributeKey<Long> ATTR_KEY_CHANNEL_ID = AttributeKey.valueOf("channelId");

    private final MessageDigester messageDigester;
    private final JacksonObjectWrapper jacksonObjectWrapper;

    /**
     * Constructor
     *
     * @param messageDigester      Message digester
     * @param jacksonObjectWrapper Jackson object wrapper
     */
    public WebSocketHandler(@NonNull MessageDigester messageDigester,
                            @NonNull JacksonObjectWrapper jacksonObjectWrapper) {

        this.messageDigester = messageDigester;
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    @Override
    protected void channelRead0(@NonNull ChannelHandlerContext ctx, @Nullable Object message) throws Exception {

        if (isNull(message)) {
            if (log.isDebugEnabled()) {
                log.debug("Empty message received");
            }
            return;
        }

        final long id = Optional.ofNullable(ctx.channel().attr(ATTR_KEY_CHANNEL_ID))
              .map(Attribute::get)
              .orElse(0L);

        if (id == 0) {
            if (log.isDebugEnabled()) {
                log.debug("Invalid channel id");
            }
            return;
        }

        // log
        if (log.isDebugEnabled()) {
            log.debug("Received message {}", message);
        }

        if (message instanceof TextWebSocketFrame frame) {
            processTextWebSocketFrame(id, frame);
        }
    }

    @Override
    public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {

        // create channel
        final ChannelInfo info = ChannelsMap.create(ctx.channel(), CS_SERVICE_ID);

        // set attribute
        requireNonNull(ctx.channel().attr(ATTR_KEY_CHANNEL_ID)).set(info.getId());

        // get host
        final String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();

        // log
        log.info("Channel id {} is active for remote address {}", info.getId(), host);

        // super
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(@NonNull ChannelHandlerContext ctx) throws Exception {

        final long id = requireNonNull(ctx.channel().attr(ATTR_KEY_CHANNEL_ID)).get();
        final String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();

        log.info("Channel id {} is inactive for remote address {}", id, host);

        // remove mapping
        ChannelsMap.removeMapping(id);

        // super
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // log
        log.error("Exception caught", cause);

        // super
        super.exceptionCaught(ctx, cause);
    }

    /**
     * Process text web socket frame
     *
     * @param id             Channel id
     * @param webSocketFrame Text web socket frame
     */
    private void processTextWebSocketFrame(long id, @NonNull TextWebSocketFrame webSocketFrame) {

        if (log.isDebugEnabled()) {
            log.debug("Frame received: {}", webSocketFrame.text());
        }

        final boolean isHandShake = jacksonObjectWrapper.isMsgType(webSocketFrame.text(), MessageConstants.HAND_SHAKE);

        final Frame frame = jacksonObjectWrapper.readValue(webSocketFrame.text(), Frame.class);
        final String payload = jacksonObjectWrapper.getPayload(webSocketFrame.text());

        final PlayerSession playerSession;
        final MessageParser messageParser = new MessageParser(payload);

        // From the cookieID get the userID from the
        // Redis

        long userId = CookieVsUserID.getUserId(frame.getAuthorizationtoken());

        if (log.isDebugEnabled()) {
            log.debug("User id: {}", userId);
        }

        if (userId <= 0) {
            // For time being will populate once the Gateway
            // dev is completed will pick from the Redis
            userId = Long.parseLong(frame.getAuthorizationtoken());
            CookieVsUserID.put(frame.getAuthorizationtoken(), Long.parseLong(frame.getAuthorizationtoken()));
        }
        // Get the ChannelId from the context
        ChannelInfo channelInfo = ChannelsMap.getChannelInfo(id);
        if (isHandShake) {
            // Now Map the Channel with UserID
            playerSession = UserIDVsChannelInfo.put(userId, channelInfo.getId(), CS_SERVICE_ID);
        } else {
            playerSession = UserIDVsChannelInfo.getPlayerSession(userId);
        }

        messageDigester.messageProcessor(playerSession, frame, messageParser);
    }
}
