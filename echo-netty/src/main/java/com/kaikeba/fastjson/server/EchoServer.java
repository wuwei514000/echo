package com.kaikeba.fastjson.server;

import com.kaikeba.fastjson.series.JsonDecoder;
import com.kaikeba.fastjson.series.JsonEncoder;
import com.kaikeba.fastjson.server.handle.EchoServerHandler;
import com.kaikeba.info.HostInfo;
import com.kaikeba.messagepack.series.MessagePackDecoder;
import com.kaikeba.messagepack.series.MessagePackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import javax.jws.soap.SOAPBinding;

public class EchoServer {
    public void run() throws Exception{ //启动服务器端
        //线程池是提升服务器性能的重要技术手段，利用定长的线程池可以保证核心的有效数量
        //在netty中的线程池分为两类：主线程池（接收客户端连接） 工作线程池（处理客户端连接）
        NioEventLoopGroup boosGroup = new NioEventLoopGroup(10); //创建接收线程池
        NioEventLoopGroup workGroup = new NioEventLoopGroup(20);
        System.out.println("服务启动成功，监听端口为：" + HostInfo.PORT);

        //启动服务器，创建服务器端的程序进行NIO启动，同事可以设置Channel
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workGroup).channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,4));
                    socketChannel.pipeline().addLast(new JsonDecoder());
                    socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast(new JsonEncoder());
                    socketChannel.pipeline().addLast(new EchoServerHandler());
                }
            });
            //可以直接利用常量进行TCP协议的相关的配置
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //异步回调机制
            ChannelFuture future = serverBootstrap.bind(HostInfo.PORT).sync();
            future.channel().closeFuture().sync(); //等待socket被关闭
        }finally {
            workGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }
}
