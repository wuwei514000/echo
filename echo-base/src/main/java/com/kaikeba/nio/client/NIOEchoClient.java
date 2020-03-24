package com.kaikeba.nio.client;

import com.kaikeb.util.InputUtil;
import com.kaikeba.info.HostInfo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOEchoClient {
    public static void main(String[] args) throws Exception{
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.connect(new InetSocketAddress(HostInfo.HOST_NAME,HostInfo.PORT));
        ByteBuffer buffer = ByteBuffer.allocate(50);
        boolean flag = true;
        while (flag){
            buffer.clear();
            String inputData = InputUtil.getString("请输入发送的信息：").trim();
            buffer.put(inputData.getBytes());
            buffer.flip();//重置缓存区
            clientChannel.write(buffer);
            buffer.clear();
            int readCount = clientChannel.read(buffer);
            buffer.flip();
            System.err.println(new String(buffer.array(),0,readCount));
            if ("byebye".equalsIgnoreCase(inputData)){
                flag = false;
            }
        }
        clientChannel.close();
    }
}
