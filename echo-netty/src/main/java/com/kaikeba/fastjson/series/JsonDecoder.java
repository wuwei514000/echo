package com.kaikeba.fastjson.series;

import com.alibaba.fastjson.JSON;
import com.kaikeba.fastjson.Member;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        int len = msg.readableBytes();
        byte[] data = new byte[len];
        msg.getBytes(msg.readerIndex(),data,0,len);
        list.add(JSON.parseObject(new String(data)).toJavaObject(Member.class));
    }
}
