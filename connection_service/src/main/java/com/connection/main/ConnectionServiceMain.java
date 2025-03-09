package com.connection.main;

public class ConnectionServiceMain
{

	public static void main( String[] args )
	{
		ConnectionServiceImpl connectionServiceImpl = ConnectionServiceImpl.init();
		connectionServiceImpl.initMessageQueue();
		connectionServiceImpl.startTheWebServer();

	}

}
