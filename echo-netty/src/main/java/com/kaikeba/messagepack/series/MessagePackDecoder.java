package com.kaikeba.messagepack.series;

import com.kaikeba.messagepack.Member;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        int len = msg.readableBytes();//读取数据长度
        byte[] data = new byte[len];//准备读取数据的空间
        msg.getBytes(msg.readerIndex(),data,0,len);//读取数据
        MessagePack msgPack = new MessagePack();
        //list.add(msgPack.read(data));
        list.add(msgPack.read(data,msgPack.lookup(Member.class)));
    }
}
