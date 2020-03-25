package com.kaikeba.messagepack.client;

import com.kaikeba.info.HostInfo;
import com.kaikeba.messagepack.client.handle.EchoClientHandler;
import com.kaikeba.messagepack.series.MessagePackDecoder;
import com.kaikeba.messagepack.series.MessagePackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class EchoClient {
    public void run() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup(); //创建线程池
        try {
            Bootstrap client = new Bootstrap(); //创建客户端程序
            client.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)  //允许接收大块数据
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,4));
                            socketChannel.pipeline().addLast(new MessagePackDecoder());
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast(new MessagePackEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture channelFuture = client.connect(HostInfo.HOST_NAME, HostInfo.PORT);
            //服务器连接状态监听
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()){
                        System.out.println("服务器已经连接完成，可以保证消息准确的传输");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        }finally {

        }
    }
}
