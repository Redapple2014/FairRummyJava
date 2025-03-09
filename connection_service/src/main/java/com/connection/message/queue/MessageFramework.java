package com.connection.message.queue;

import java.io.Closeable;

public interface MessageFramework extends Closeable
{

	public boolean registerQueuePublisher( String queueName );

	public boolean publishToQueue( String queueName, String message );

	public boolean registerQueueConsumer( String queueName, MessageHandler messageHandler );

}
