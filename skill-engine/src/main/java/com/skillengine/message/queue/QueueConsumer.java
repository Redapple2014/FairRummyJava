package com.skillengine.message.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class QueueConsumer implements Consumer
{

	private Connection connection;
	private Channel channel;
	protected CustomMQConsumer consumer;
	private ConsumerListernerThread listernerThread;

	public QueueConsumer( Connection connection, String queueName, MessageHandler handler )
	{
		try
		{
			System.out.println( "QueueConsumer" + queueName );
			channel = connection.createChannel();
			DeclareOk declareOk = channel.queueDeclare( queueName, true, false, false, null );
			this.consumer = new CustomMQConsumer( channel );
			channel.basicConsume( queueName, false, consumer );
			listernerThread = new ConsumerListernerThread( consumer, queueName, handler );
			listernerThread.start();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException
	{
		listernerThread.stopListening();
		listernerThread.stopWorkers();
		try
		{
			channel.close();
		}
		catch( IOException | TimeoutException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
