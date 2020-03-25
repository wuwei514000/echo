package com.kaikeba.separator.client.handle;

import com.kaikeba.info.HostInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final int REPEAT = 500;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < REPEAT; i++){
            String data= ("【" + i + "】Hello World").trim() + HostInfo.SEPARATOR;
            ctx.writeAndFlush(data);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要服务器发送完成信息之后，就会执行此方法内容的输出操作
        try {
            System.out.println(msg.toString().trim());
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
