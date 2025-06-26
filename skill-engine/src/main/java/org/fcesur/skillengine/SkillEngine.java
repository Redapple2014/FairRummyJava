package org.fcesur.skillengine;

import org.fcesur.skillengine.dao.TableDetailsDAO;
import org.fcesur.skillengine.http.APIServer;
import org.fcesur.skillengine.message.parsers.Jackson;
import org.fcesur.skillengine.message.queue.GameMessageHandler;
import org.fcesur.skillengine.message.queue.RabbitMQMessageFramework;
import org.fcesur.skillengine.rummy.message.MessageDispatcher;
import org.fcesur.skillengine.service.BoardCreationService;
import org.fcesur.skillengine.service.CurrencyService;
import org.fcesur.skillengine.service.CurrencyServiceImpl;
import org.fcesur.skillengine.service.message.ServiceHandler;
import org.fcesur.skillengine.service.message.ServiceMessageDigester;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.fcesur.model.ServiceType.CONN_SERVICE;
import static org.fcesur.model.ServiceType.GAME_SERVICE;
import static org.fcesur.model.ServiceType.GCS_SERVICE;

@Getter
public final class SkillEngine implements AutoCloseable {

    // private static final String QUEUE_PUBLISHER = CONN_SERVICE;
    // private static final String QUEUE_GCS_PUBLISHER = GCS_SERVICE;
    // private static final String QUEUE_CONSUMER = GAME_SERVICE;

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

        messageFramework.registerQueuePublisher(CONN_SERVICE);
        messageFramework.registerQueuePublisher(GCS_SERVICE);
        messageFramework.registerQueueConsumer(GAME_SERVICE, new GameMessageHandler());
    }

    public TableDetailsDAO getTableDetailsDAO() {
        return detailsDAO;
    }

    @Override
    public void close() throws Exception {
        // TODO
    }
}
