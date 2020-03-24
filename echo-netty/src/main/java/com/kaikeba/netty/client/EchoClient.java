package com.kaikeba.netty.client;

import com.kaikeba.info.HostInfo;
import com.kaikeba.netty.client.handle.EchoClientHandler;
import com.kaikeba.netty.server.handle.EchoServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
    public void run() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup(); //创建线程池
        try {
            Bootstrap client = new Bootstrap(); //创建客户端程序
            client.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)  //允许接收大块数据
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler()); // 追加处理器
                        }
                    });
            ChannelFuture channelFuture = client.connect(HostInfo.HOST_NAME, HostInfo.PORT);
            channelFuture.channel().closeFuture().sync();
        }finally {

        }
    }
}
