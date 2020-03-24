package com.kaikeba.netty.server;

import com.kaikeba.info.HostInfo;
import com.kaikeba.netty.server.handle.EchoServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
           //定义子处理器
           serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
               protected void initChannel(SocketChannel socketChannel) throws Exception {
                   socketChannel.pipeline().addLast(new EchoServerHandler()); // 追加处理器
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
