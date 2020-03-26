package com.kaikeba.server;

import com.kaikeba.info.HostInfo;
import com.kaikeba.server.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HTTPServer {
    public void run() throws Exception {
        NioEventLoopGroup bosGroup = new NioEventLoopGroup(10);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(20);
        System.out.println("服务器连接成功，连接端口为：" + HostInfo.PORT);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bosGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseEncoder()); //响应编码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder()); //响应解码
                            //socketChannel.pipeline().addLast(new ChunkedWriteHandler());//图片传输处理器
                            socketChannel.pipeline().addLast(new HttpServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = serverBootstrap.bind(HostInfo.PORT);
            future.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bosGroup.shutdownGracefully();
        }

    }
}
