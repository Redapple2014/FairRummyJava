package com.skillengine.main;

import com.skillengine.dao.TableDetailsDAO;
import com.skillengine.dao.UserStatsDAO;
import com.skillengine.http.APIServer;
import com.skillengine.message.parsers.Jackson;
import com.skillengine.message.queue.GameMessageHandler;
import com.skillengine.message.queue.RabbitMQFrameworkImpl;
import com.skillengine.rummy.message.MessageDispatcher;
import com.skillengine.service.BoardCreationService;
import com.skillengine.service.CurrencyService;
import com.skillengine.service.CurrencyServiceImpl;
import com.skillengine.service.message.ServiceHandler;
import com.skillengine.service.message.ServiceMessageDigester;

public class SkillEngineImpl
{
	private static SkillEngineImpl instance = null;

	private Jackson jackson;
	private ServiceMessageDigester digester;
	private CurrencyService currencyService;
	private ServiceHandler handler;
	private MessageDispatcher dispatcher;
	private RabbitMQFrameworkImpl frameworkImpl;
	private TableDetailsDAO detailsDAO;
	private UserStatsDAO statsDAO;
	private BoardCreationService boardCreationService;
	private APIServer apiServer;

	public static SkillEngineImpl init()
	{
		if( instance == null )
		{
			instance = new SkillEngineImpl();
		}
		return instance;
	}

	public SkillEngineImpl()
	{
		jackson = new Jackson();
		digester = new ServiceMessageDigester( jackson );
		CurrencyService currencyService = new CurrencyServiceImpl();
		handler = new ServiceHandler( digester, currencyService, jackson );
		dispatcher = new MessageDispatcher( jackson );
		detailsDAO = new TableDetailsDAO();
		statsDAO = new UserStatsDAO();
		boardCreationService = new BoardCreationService();
		apiServer = new APIServer( jackson );
		apiServer.init();
	}

	/**
	 * @return the apiServer
	 */
	public APIServer getApiServer()
	{
		return apiServer;
	}

	/**
	 * @return the dispatcher
	 */
	public MessageDispatcher getDispatcher()
	{
		return dispatcher;
	}

	/**
	 * @return the currencyService
	 */
	public CurrencyService getCurrencyService()
	{
		return currencyService;
	}

	/**
	 * @return the handler
	 */
	public ServiceHandler getHandler()
	{
		return handler;
	}

	/**
	 * @return the jackson
	 */
	public Jackson getJackson()
	{
		return jackson;
	}

	/**
	 * @return the digester
	 */
	public ServiceMessageDigester getDigester()
	{
		return digester;
	}

	public static SkillEngineImpl getInstance()
	{
		return instance;
	}

	public void initMessageQueue()
	{
		try
		{
			String queuePublisher = "cs";
			String queueGcsPublisher = "gcs";
			String queueConsumer = "ge";
			frameworkImpl = new RabbitMQFrameworkImpl( "" );
			frameworkImpl.registerQueuePublisher( queuePublisher );
			frameworkImpl.registerQueuePublisher( queueGcsPublisher );
			frameworkImpl.registerQueueConsumer( queueConsumer, new GameMessageHandler() );

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the frameworkImpl
	 */
	public RabbitMQFrameworkImpl getFrameworkImpl()
	{
		return frameworkImpl;
	}

	public TableDetailsDAO getTableDetailsDAO()
	{
		return detailsDAO;
	}

	/**
	 * @return the boardCreationService
	 */
	public BoardCreationService getBoardCreationService()
	{
		return boardCreationService;
	}

	/**
	 * @return the statsDAO
	 */
	public UserStatsDAO getStatsDAO()
	{
		return statsDAO;
	}

}
