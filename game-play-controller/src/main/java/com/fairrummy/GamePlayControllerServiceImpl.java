package com.fairrummy;

/*import com.fairrummy.message.queue.GCSMessageHandler;
import com.fairrummy.message.queue.RabbitMQFrameworkImpl;
import com.fairrummy.mq.MQFrameworkFactory;
import com.fairrummy.service.message.ServiceHandler;
import com.fairrummy.service.message.ServiceMessageDigester;
import com.fairrummy.service.message.parsers.Jackson;
import com.fairrummy.socket.message.MessageDispatcher;
import com.fairrummy.utility.EligibilityMessageHandler;

public class GamePlayControllerServiceImpl {
    private static GamePlayControllerServiceImpl instance = null;

    private Jackson jackson;
    private ServiceMessageDigester digester;
    private ServiceHandler handler;
    private MessageDispatcher dispatcher;
    private RabbitMQFrameworkImpl frameworkImpl;

    public static GamePlayControllerServiceImpl init()
    {
        if( instance == null )
        {
            instance = new GamePlayControllerServiceImpl();
        }
        return instance;
    }

    public GamePlayControllerServiceImpl()
    {
        jackson = new Jackson();
        digester = new ServiceMessageDigester( jackson );
        handler = new ServiceHandler( digester, jackson );
        dispatcher = new MessageDispatcher( jackson );
    }

    public MessageDispatcher getDispatcher()
    {
        return dispatcher;
    }

    public ServiceHandler getHandler()
    {
        return handler;
    }

    public Jackson getJackson()
    {
        return jackson;
    }

    public ServiceMessageDigester getDigester()
    {
        return digester;
    }

    public static GamePlayControllerServiceImpl getInstance()
    {
        return instance;
    }

    public void initMessageQueue()
    {
        try
        {
            String queuePublisher = "cs";
            String queueConsumer = "gcs";
            String geQueuePublisher = "ge";
            frameworkImpl = new RabbitMQFrameworkImpl("");
            frameworkImpl.registerQueuePublisher( queuePublisher );
            frameworkImpl.registerQueuePublisher( geQueuePublisher );
            frameworkImpl.registerQueueConsumer( queueConsumer, new GCSMessageHandler() );

            MQFrameworkFactory.init();
            MQFrameworkFactory.getFramework().registerTopicCosumer("eligibilityQueue", new EligibilityMessageHandler());
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    public RabbitMQFrameworkImpl getFrameworkImpl()
    {
        return frameworkImpl;
    }
}*/
