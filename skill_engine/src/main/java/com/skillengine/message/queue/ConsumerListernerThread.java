package com.skillengine.message.queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsumerListernerThread extends Thread
{
	private CustomMQConsumer consumer;
	private MessageHandler handler;
	private ExecutorService workerThreadPool;
	private boolean listening = true;

	public ConsumerListernerThread( CustomMQConsumer consumer, String queueName, MessageHandler handler )
	{
		log.info( "Queuename {}", queueName );
		this.consumer = consumer;
		this.handler = handler;
		int queueSize = 1000;
		int workerThreads = Runtime.getRuntime().availableProcessors();
		int maxWorkerThreads = workerThreads * 2;
		long keepAliveTime = 60000;
		ThreadFactory threadFactory = new ThreadFactory()
		{
			final AtomicInteger threadNumber = new AtomicInteger( 1 );

			@Override
			public Thread newThread( Runnable r )
			{
				return new Thread( r, "MessageReceiver-" + threadNumber.getAndIncrement() );
			}
		};

		BlockingQueue< Runnable > queue = null;
		if( queueSize > 0 )
		{
			queue = new ArrayBlockingQueue< Runnable >( queueSize );
		}
		else
		{
			queue = new LinkedBlockingQueue< Runnable >();
		}
		workerThreadPool = new ThreadPoolExecutor( workerThreads, maxWorkerThreads, keepAliveTime, TimeUnit.MILLISECONDS, queue, threadFactory, new ThreadPoolExecutor.AbortPolicy() );
	}

	void stopListening()
	{
		this.listening = false;
	}

	public void stopWorkers()
	{
		workerThreadPool.shutdown();
	}

	@Override
	public void run()
	{
		while( listening )
		{
			byte[] bytes = null;
			ObjectInputStream ois = null;
			CustomMQConsumer.Delivery delivery = null;
			try
			{
				delivery = consumer.nextMessage();
				bytes = delivery.getBody();
				String incomingMsg = new String( bytes, "UTF-8" );
				workerThreadPool.execute( new Runnable()
				{
					@Override
					public void run()
					{
						handler.handleMessage( incomingMsg );

					}
				} );
			}
			catch( Exception e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				if( delivery != null && consumer.getChannel() != null )
				{
					try
					{
						// System.out.println("AutoAck
						// is false : "+autoAck);
						consumer.getChannel().basicAck( delivery.getEnvelope().getDeliveryTag(), true );
					}
					catch( IOException e )
					{
						e.printStackTrace();
					}
				}
			}
		}

	}

}
