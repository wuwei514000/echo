package com.kaikeba.client;

import com.kaikeba.client.handler.HttpClientHandler;
import com.kaikeba.info.HostInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class HttpClient {
    public void run() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup(); //创建线程池
        try {
            Bootstrap client = new Bootstrap(); //创建客户端程序
            client.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpResponseDecoder());
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            socketChannel.pipeline().addLast(new HttpClientHandler());
                        }
                    });
            ChannelFuture channelFuture = client.connect(HostInfo.HOST_NAME, HostInfo.PORT);
            //http访问地址
            String url = "http://" + HostInfo.HOST_NAME + ":" + HostInfo.PORT;
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
            request.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH,String.valueOf(request.content().readableBytes()));
            request.headers().set(HttpHeaderNames.COOKIE,"nothing");
            channelFuture.channel().writeAndFlush(request);
            channelFuture.channel().closeFuture().sync();
        }finally {

        }
    }
}
