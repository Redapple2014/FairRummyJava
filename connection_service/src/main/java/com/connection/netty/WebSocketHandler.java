package com.connection.netty;

import java.net.InetSocketAddress;

import com.connection.cookie.CookieVsUserID;
import com.connection.jackson.JacksonObjectWrapper;
import com.connection.msg.Frames;
import com.connection.msg.MessageConstants;
import com.connection.msg.MessageDigester;
import com.connection.msg.MessageParser;
import com.connection.netty.channels.ChannelIDChannelInfoMap;
import com.connection.netty.channels.ChannelInfo;
import com.connection.netty.channels.UserIDVsChannelInfo;
import com.connection.services.PlayerSession;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler< Object >
{

	private static final AttributeKey< Long > channelIdAttrkey = AttributeKey.valueOf( "channelId" );

	private static final int CS_SERVICE_ID = 1;

	private JacksonObjectWrapper jacksonObjectWrapper;
	private MessageDigester messageDigester;

	public WebSocketHandler( JacksonObjectWrapper jacksonObjectWrapper, MessageDigester messageDigester )
	{
		this.jacksonObjectWrapper = jacksonObjectWrapper;
		this.messageDigester = messageDigester;
	}

	@Override
	public void channelInactive( ChannelHandlerContext ctx ) throws Exception
	{
		Attribute< Long > attr = ctx.channel().attr( channelIdAttrkey );
		long channelId = attr.get();
		String host = ( ( InetSocketAddress ) ctx.channel().remoteAddress() ).getAddress().getHostAddress();
		log.info( "ChannelID is Inactive {} RemoteAddress {}", channelId, host );
		ChannelIDChannelInfoMap.removeMapping( channelId );
		super.channelInactive( ctx );

	}

	@Override
	public void channelActive( ChannelHandlerContext ctx ) throws Exception
	{
		ChannelInfo channelInfo = ChannelIDChannelInfoMap.createChannelIDAndAddInfoToMap( ctx.channel(), CS_SERVICE_ID );
		Attribute< Long > attr = ctx.channel().attr( channelIdAttrkey );
		attr.set( channelInfo.getChannelID() );
		String host = ( ( InetSocketAddress ) ctx.channel().remoteAddress() ).getAddress().getHostAddress();
		log.info( "Channel Active {} Remote Address {}", channelInfo.getChannelID(), host );
		super.channelActive( ctx );
	}

	@Override
	public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception
	{
		// TODO Auto-generated method stub
		super.exceptionCaught( ctx, cause );
	}

	@Override
	protected void channelRead0( ChannelHandlerContext ctx, Object msg ) throws Exception
	{
		Attribute< Long > attr = ctx.channel().attr( channelIdAttrkey );
		long channelId = attr.get();
		System.out.println( "Received the message" + msg );
		log.info( "Received the message {}", msg );
		if( msg == null )
		{
			System.out.println( "Null from msg" + msg );
			return;
		}
		if( msg instanceof TextWebSocketFrame )
		{
			log.info( "Received the message inside textwebsocketframe" );
			TextWebSocketFrame socketFrame = ( TextWebSocketFrame ) msg;
			System.out.println( "Received" + socketFrame.text() );
			Frames frames = jacksonObjectWrapper.readValue( socketFrame.text(), Frames.class );
			String receivedMsg = socketFrame.text();
			log.info( "Received the message" + frames );
			String remainingPayload = jacksonObjectWrapper.getPayload( receivedMsg );
			MessageParser messageParser = new MessageParser( remainingPayload );
			boolean isHandShake = jacksonObjectWrapper.isMsgType( receivedMsg, MessageConstants.HAND_SHAKE );
			PlayerSession playerSession = null;
			// From the cookieID get the userID from the
			// Redis
			long userId = CookieVsUserID.getUserId( frames.getAuthorizationtoken() );
			System.out.println( "UserId " + userId );
			if( userId <= 0 )
			{
				// For time being will populate once the Gateway
				// dev is completed will pick from the Redis
				userId = Long.parseLong( frames.getAuthorizationtoken() );
				CookieVsUserID.put( frames.getAuthorizationtoken(), Long.parseLong( frames.getAuthorizationtoken() ) );
			}
			// Get the ChannelId from the context
			ChannelInfo channelInfo = ChannelIDChannelInfoMap.getChannelInfo( channelId );
			if( isHandShake )
			{
				// Now Map the Channel with UserID
				playerSession = UserIDVsChannelInfo.put( userId, channelInfo.getChannelID(), CS_SERVICE_ID );
			}
			else
			{
				playerSession = UserIDVsChannelInfo.getPlayerSession( userId );
			}
			messageDigester.messageProcessor( playerSession, frames, messageParser );

		}

	}

}
