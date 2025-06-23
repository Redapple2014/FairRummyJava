package com.connection.netty;

import com.connection.jackson.JacksonObjectWrapper;
import com.connection.msg.MessageDigester;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitializer {
    private final int port;
    private final int BOSS_THREADS = 1;
    private final int WORKER_THREADS = 2;
    private JacksonObjectWrapper jacksonObjectWrapper;
    private MessageDigester messageDigester;

    public ServerInitializer(int port, JacksonObjectWrapper jacksonObjectWrapper, MessageDigester messageDigester) {
        this.port = port;
        this.jacksonObjectWrapper = jacksonObjectWrapper;
        this.messageDigester = messageDigester;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS);
        EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_THREADS);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast("http-codec", new HttpServerCodec());
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("websocketprotocolhandler", new WebSocketServerProtocolHandler("/ws"));
                    ch.pipeline().addLast("handler", new WebSocketHandler(jacksonObjectWrapper, messageDigester));
                }
            });

            System.out.println("WebSocket server started at port " + port);
            b.bind(port).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
