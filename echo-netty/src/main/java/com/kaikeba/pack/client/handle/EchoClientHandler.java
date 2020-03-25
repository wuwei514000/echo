package com.kaikeba.pack.client.handle;

import com.kaikeb.util.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final int REPEAT = 500;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < REPEAT; i++){
            byte[] data = ("【" + i + "】Hello World" + System.getProperty("line.separator")).getBytes();
            ByteBuf dataBuf = Unpooled.buffer(data.length);
            dataBuf.writeBytes(data);
            ctx.writeAndFlush(dataBuf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只要服务器发送完成信息之后，就会执行此方法内容的输出操作
        try {
            ByteBuf readBuf = (ByteBuf) msg;
            String readData = readBuf.toString(CharsetUtil.UTF_8).trim();
            System.out.println(readData);
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
