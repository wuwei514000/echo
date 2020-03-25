package com.kaikeba.fastjson.client.handle;

import com.kaikeba.fastjson.Member;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final int REPEAT = 500;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Member member = new Member();
        member.setMid("小李老师");
        member.setAge(12);
        member.setSalary(12001.1);
        member.setName("Henrry");
        ctx.writeAndFlush(member);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要服务器发送完成信息之后，就会执行此方法内容的输出操作
        try {
            Member member = (Member) msg;
            System.out.println(member);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
