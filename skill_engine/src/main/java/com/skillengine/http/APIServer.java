package com.skillengine.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.skillengine.http.handler.TemplateHandler;
import com.skillengine.message.parsers.Jackson;
import com.sun.net.httpserver.HttpServer;

public class APIServer
{
	private Jackson jsonparser;

	public APIServer( Jackson jackson )
	{
		this.jsonparser = jackson;
	}

	public void init()
	{
		HttpServer server;
		try
		{
			server = HttpServer.create( new InetSocketAddress( 8082 ), 0 );
			server.createContext( "/v1/tablecreate", new TemplateHandler( jsonparser ) );
			server.setExecutor( null );
			server.start();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
		System.out.println( "Server started at http://localhost:8082" );
	}

}
