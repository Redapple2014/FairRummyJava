package com.skillengine.http.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.skillengine.dto.BoardCreationInfo;
import com.skillengine.dto.TemplateInfo;
import com.skillengine.main.SkillEngineImpl;
import com.skillengine.message.parsers.Jackson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TemplateHandler implements HttpHandler
{

	private Jackson parser;

	public TemplateHandler( Jackson jackson )
	{
		super();
		this.parser = jackson;
	}

	@Override
	public void handle( HttpExchange exchange ) throws IOException
	{
		if( "POST".equalsIgnoreCase( exchange.getRequestMethod() ) )
		{
			InputStream inputStream = exchange.getRequestBody();
			TemplateInfo info = parser.readValue( inputStream, TemplateInfo.class );
			BoardCreationInfo boardCreationInfo = SkillEngineImpl.getInstance().getBoardCreationService().boardCreation( info.getTemplateId() );
			String response = parser.writeValueAsString( boardCreationInfo );
			exchange.getResponseHeaders().set( "Content-Type", "application/json" );
			exchange.sendResponseHeaders( 200, response.length() );
			try (OutputStream os = exchange.getResponseBody())
			{
				os.write( response.getBytes() );
			}
		}
	}

}
