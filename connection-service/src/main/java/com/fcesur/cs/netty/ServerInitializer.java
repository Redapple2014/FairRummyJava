package com.fcesur.cs.netty;

import com.fcesur.cs.jackson.JacksonObjectWrapper;
import com.fcesur.cs.msg.MessageDigester;
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
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * Server initializer
 */
@Slf4j
public class ServerInitializer {

    private static final int BOSS_THREADS = 1;
    private static final int WORKER_THREADS = 2;

    private final int port;
    private final JacksonObjectWrapper jacksonObjectWrapper;
    private final MessageDigester messageDigester;

    /**
     * Constructor
     *
     * @param port                 Port
     * @param jacksonObjectWrapper Jackson object wrapper
     * @param messageDigester      Message digester
     */
    public ServerInitializer(int port, @NonNull JacksonObjectWrapper jacksonObjectWrapper, @NonNull MessageDigester messageDigester) {
        this.port = port;
        this.jacksonObjectWrapper = jacksonObjectWrapper;
        this.messageDigester = messageDigester;
    }

    /**
     * Start the web server
     *
     * @throws InterruptedException Throws when interrupted
     */
    public void start() throws InterruptedException {

        final EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS);
        final EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_THREADS);

        try {

            ServerBootstrap b = new ServerBootstrap()
                  .group(bossGroup, workerGroup)
                  .channel(NioServerSocketChannel.class)
                  .childHandler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      public void initChannel(SocketChannel ch) {
                          ch.pipeline().addLast("http-codec", new HttpServerCodec());
                          ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                          ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                          ch.pipeline().addLast("websocketprotocolhandler", new WebSocketServerProtocolHandler("/ws"));
                          ch.pipeline().addLast("handler", new WebSocketHandler(messageDigester, jacksonObjectWrapper));
                      }
                  });

            log.info("WebSocket server started at port {}", port);

            b.bind(port)
                  .sync()
                  .channel()
                  .closeFuture()
                  .sync();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
