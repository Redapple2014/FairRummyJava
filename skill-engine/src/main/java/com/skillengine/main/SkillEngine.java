package com.skillengine.main;

import com.skillengine.dao.TableDetailsDAO;
import com.skillengine.http.APIServer;
import com.skillengine.message.parsers.Jackson;
import com.skillengine.message.queue.GameMessageHandler;
import com.skillengine.message.queue.RabbitMQMessageFramework;
import com.skillengine.rummy.message.MessageDispatcher;
import com.skillengine.service.BoardCreationService;
import com.skillengine.service.CurrencyService;
import com.skillengine.service.CurrencyServiceImpl;
import com.skillengine.service.message.ServiceHandler;
import com.skillengine.service.message.ServiceMessageDigester;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Getter
public final class SkillEngine implements AutoCloseable {

    private static final String QUEUE_PUBLISHER = "cs";
    private static final String QUEUE_GCS_PUBLISHER = "gcs";
    private static final String QUEUE_CONSUMER = "ge";

    private static SkillEngine INSTANCE = null;

    private final Jackson jackson;
    private final ServiceMessageDigester digester;
    private CurrencyService currencyService;
    private final ServiceHandler handler;
    private final MessageDispatcher dispatcher;
    private RabbitMQMessageFramework messageFramework;
    private final TableDetailsDAO detailsDAO;
    private final BoardCreationService boardCreationService;
    private final APIServer apiServer;

    /**
     * Initialize the skill engine
     *
     * @return Instance of skill engine
     */
    public static SkillEngine init() {
        if (INSTANCE == null) {
            INSTANCE = new SkillEngine();
        }
        return INSTANCE;
    }

    /**
     * Get instance of skill engine
     *
     * @return Instance of skill engine
     */
    public static SkillEngine getInstance() {
        return INSTANCE;
    }

    public SkillEngine() {
        jackson = new Jackson();
        digester = new ServiceMessageDigester(jackson);
        CurrencyService currencyService = new CurrencyServiceImpl();
        handler = new ServiceHandler(digester, currencyService, jackson);
        dispatcher = new MessageDispatcher(jackson);
        detailsDAO = new TableDetailsDAO();
        boardCreationService = new BoardCreationService();
        apiServer = new APIServer(jackson);
        apiServer.init();
    }

    public void initMessageQueue() throws IOException, TimeoutException {

        messageFramework = new RabbitMQMessageFramework();

        messageFramework.registerQueuePublisher(QUEUE_PUBLISHER);
        messageFramework.registerQueuePublisher(QUEUE_GCS_PUBLISHER);
        messageFramework.registerQueueConsumer(QUEUE_CONSUMER, new GameMessageHandler());
    }

    public TableDetailsDAO getTableDetailsDAO() {
        return detailsDAO;
    }

    @Override
    public void close() throws Exception {
        // TODO
    }
}
