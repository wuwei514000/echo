package com.kaikeba.strpack.server.handle;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //表示要进行数据信息读取操作，对于读取操作完成后也可以直接回应
            //对于客户端发送过来的信息，由于没有进行指定数据类型，所以都按照Object接收
            String inputData = msg.toString().trim();
            System.out.println("{客户端}：" + inputData);
            String echoData = ("【ECHO】" + inputData ).trim() + System.getProperty("line.separator");
            ctx.writeAndFlush(echoData); //回应输出操作
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
