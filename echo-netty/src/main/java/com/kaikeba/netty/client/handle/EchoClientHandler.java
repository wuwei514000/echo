package com.kaikeba.netty.client.handle;

import com.kaikeb.util.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要服务器发送完成信息之后，就会执行此方法内容的输出操作
        try {
            ByteBuf readBuf = (ByteBuf) msg;
            String readData = readBuf.toString(CharsetUtil.UTF_8).trim();
            if ("quit".equalsIgnoreCase(readData)){
                System.out.println("【EXIT】 拜拜您已经结束了本次网络通讯，再见！");
                ctx.close();
            }else{
                System.out.println(readData);
                String inpuData = InputUtil.getString("请输入要发送的数据：");
                byte[] data = inpuData.getBytes();
                ByteBuf sendBuf = Unpooled.buffer(data.length);
                sendBuf.writeBytes(data);
                ctx.writeAndFlush(sendBuf);
            }
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
