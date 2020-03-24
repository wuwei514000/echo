package com.kaikeba.netty.server.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理Echo的操作方式ChannelInboundHandlerAdapter是针对数据写入的操作
 * netty是基于NIO一种开发框架的封装，这里面适合AIO还没有任何关系的
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当客户端连接成功后调用此方法明确可以给客户端发送一些信息
        byte data[] = "【服务器激活信息】 连接通道已经创建，服务器开始进行响应交互。".getBytes();
        //nio是基于缓存的操作，netty也有一些列的缓存，封装了nio的buffer
        ByteBuf buf = Unpooled.buffer(data.length); //netty自己封装的缓存
        buf.writeBytes(data);//将数据写入缓存
        ctx.writeAndFlush(buf); //强制性的发送所有数据

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //表示要进行数据信息读取操作，对于读取操作完成后也可以直接回应
            //对于客户端发送过来的信息，由于没有进行指定数据类型，所以都按照Object接收
            ByteBuf buf = (ByteBuf) msg;
            //指定编码
            //Charset.defaultCharset();
            String inputData = buf.toString(CharsetUtil.UTF_8);//将字节缓冲区的内容转为字符串
            System.out.println("客户端请求数据：" + inputData);
            String echoData = "【ECHO】" + inputData;
            if ("exit".equalsIgnoreCase(inputData)) {
                echoData = "quit";
            }
            byte[] data = echoData.getBytes();
            ByteBuf echoBuf = Unpooled.buffer(data.length);
            echoBuf.writeBytes(data); //将内容写入缓存之中
            ctx.writeAndFlush(echoBuf); //回应输出操作
        }finally {
            ReferenceCountUtil.release(msg); //释放缓存
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
