/**
 *
 */
package com.fcesur.cs.msg.dispatcher;

import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.main.ConnectionService;
import com.fcesur.cs.msg.Frames;
import com.fcesur.cs.msg.MessageParser;
import com.fcesur.cs.msg.SessionDetails;
import com.fcesur.cs.netty.channels.UserIDVsChannelInfo;
import com.fcesur.cs.services.PlayerSession;
import com.fcesur.cs.services.ServiceMessage;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class CS2ServiceDispatcher {
    private ExecutorService executorGameEndService;
    private final ConcurrentMap<String, DispatcherTask> serviceVSDispatcherMap = new ConcurrentHashMap<String, DispatcherTask>();
    private JacksonObjectWrapper jacksonObjectWrapper;

    public CS2ServiceDispatcher(JacksonObjectWrapper jacksonObjectWrapper) {
        int threadPoolCount = 10;
        executorGameEndService = Executors.newFixedThreadPool(threadPoolCount);
        this.jacksonObjectWrapper = jacksonObjectWrapper;
    }

    public void enQueue(Frames frames, MessageParser messageParser) {
        String userId = frames.getAuthorizationtoken();
        String serviceType = frames.getServiceType();
        DispatcherTask dispatcherTask = null;
        DispatcherTask existingDispatcherTask = serviceVSDispatcherMap.get(serviceType);
        if (existingDispatcherTask != null) {

            SessionDetails details = new SessionDetails(Long.parseLong(userId), messageParser.getSrvMsg());
            existingDispatcherTask.messageQueue.add(details);
            dispatcherTask = existingDispatcherTask;
        } else {
            SessionDetails details = new SessionDetails(Long.parseLong(userId), messageParser.getSrvMsg());
            dispatcherTask = new DispatcherTask(jacksonObjectWrapper);
            dispatcherTask.messageQueue.add(details);
        }
        executorGameEndService.submit(dispatcherTask);
    }

    public void deactivateService(String serviceID) {
        serviceVSDispatcherMap.remove(serviceID);
    }

    private static class DispatcherTask implements Runnable {
        private BlockingQueue<SessionDetails> messageQueue = new LinkedBlockingQueue<SessionDetails>();

        private JacksonObjectWrapper jacksonObjectWrapper;

        public DispatcherTask(JacksonObjectWrapper jacksonObjectWrapper) {
            this.jacksonObjectWrapper = jacksonObjectWrapper;
        }

        @Override
        public void run() {

            synchronized (messageQueue) {
                ArrayList<SessionDetails> sessionFramePairs = new ArrayList<SessionDetails>(messageQueue.size());
                messageQueue.drainTo(sessionFramePairs);
                for (SessionDetails details : sessionFramePairs) {
                    PlayerSession playerSession = UserIDVsChannelInfo.getPlayerSession(details.getUserId());
                    String userSession = jacksonObjectWrapper.writeValueAsString(playerSession);
                    ServiceMessage message = new ServiceMessage(details.getPayload(), userSession, -1l);
                    String srvMsg = jacksonObjectWrapper.writeValueAsString(message);
                    ConnectionService.getInstance().getServiceDispatcher().enQueue(null, null);
                }

            }
        }
    }
}
