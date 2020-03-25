package com.kaikeba.fastjson.server.handle;

import com.kaikeba.fastjson.Member;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理Echo的操作方式ChannelInboundHandlerAdapter是针对数据写入的操作
 * netty是基于NIO一种开发框架的封装，这里面适合AIO还没有任何关系的
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Member member = (Member) msg;
            System.out.println("{客户端}：" + member);
            member.setName("【ECHO】" + member.getName());
            ctx.writeAndFlush(member); //回应输出操作
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
