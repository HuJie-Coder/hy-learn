package com.hy.netty.start;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * DiscardServer
 *
 * @author Jie.Hu
 * @date 8/6/21 10:32 PM
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }


    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGrouop = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGrouop)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
            ;

            // bind and start to accept incoming connections
            ChannelFuture future = serverBootstrap.bind(port).sync();

            // Wait until the server socket is closed

            future.channel().closeFuture().sync();

        } finally {
            workerGrouop.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        int port = 8080;

        if(args.length > 0 ) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }

}
