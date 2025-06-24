package com.fcesur.cs.msg.dispatcher;

import com.fcesur.cs.msg.SessionDetails;
import com.fcesur.cs.netty.channels.ChannelIDChannelInfoMap;
import com.fcesur.cs.netty.channels.ChannelInfo;
import com.fcesur.cs.netty.channels.UserIDVsChannelInfo;
import com.fcesur.cs.services.PlayerSession;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CS2ClientDispatcher {
    private ExecutorService executorClientDispatcher;
    private static Logger logger = LoggerFactory.getLogger(CS2ClientDispatcher.class);
    private final ConcurrentMap<Long, ClientDispatcher> serviceVSDispatcherMap = new ConcurrentHashMap<Long, ClientDispatcher>();

    public CS2ClientDispatcher() {
        int threadPoolCount = 5;
        executorClientDispatcher = Executors.newFixedThreadPool(threadPoolCount);
    }

    public void enQueue(SessionDetails sessionDetails) {
        ClientDispatcher clientDispatcherTask = null;
        ClientDispatcher existingClientDispatcherTask = serviceVSDispatcherMap.get(sessionDetails.getUserId());
        if (existingClientDispatcherTask != null) {
            existingClientDispatcherTask.messageQueue.add(sessionDetails);
            clientDispatcherTask = existingClientDispatcherTask;
        } else {

            clientDispatcherTask = new ClientDispatcher();
            clientDispatcherTask.messageQueue.add(sessionDetails);
        }
        executorClientDispatcher.submit(clientDispatcherTask);
    }

    public void removeChannel(long userId) {
        try {
            serviceVSDispatcherMap.remove(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientDispatcher implements Runnable {

        private BlockingQueue<SessionDetails> messageQueue = new LinkedBlockingQueue<SessionDetails>();

        @Override
        public void run() {

            synchronized (messageQueue) {
                List<SessionDetails> sessionDetails = new ArrayList<SessionDetails>(messageQueue.size());
                messageQueue.drainTo(sessionDetails);
                for (SessionDetails session : sessionDetails) {
                    try {
                        PlayerSession playerSession = UserIDVsChannelInfo.getPlayerSession(session.getUserId());
                        if (playerSession != null) {
                            ChannelInfo channelInfo = ChannelIDChannelInfoMap.getChannelInfo(playerSession.getChannelID());
                            logger.debug("OutGoing channelInfo Message:" + new String(session.getPayload()));
                            if (channelInfo == null) {
                                return;
                            }
                            Channel c = channelInfo.getChannel();
                            if (c != null && c.isActive() && c.isWritable()) {
                                c.writeAndFlush(new TextWebSocketFrame(session.getPayload())).addListener(new GenericFutureListener<Future<? super Void>>() {
                                    @Override
                                    public void operationComplete(Future<? super Void> future) throws Exception {
                                    }
                                });
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
