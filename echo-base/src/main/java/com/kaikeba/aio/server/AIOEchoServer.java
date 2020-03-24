package com.kaikeba.aio.server;


import com.kaikeba.info.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

class EchoHandler implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel clientChannel;
    private boolean exit = false;

    public EchoHandler(AsynchronousSocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        String readMessage = new String(buffer.array(),0,buffer.remaining()).trim();
        System.out.println("【客户端请求】：" + readMessage);
        String writeMessage = "【ECHO】" + readMessage + "\n";
        if("byebye".equalsIgnoreCase(readMessage)){
            writeMessage = "【EXIT】拜拜下次见" + "\n";
            this.exit = true;
        }
        this.echoWrite(writeMessage);
    }

    public void echoWrite(String content){
        final ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put(content.getBytes());
        buffer.flip();
        this.clientChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buf) {
                if (buffer.hasRemaining()){
                    EchoHandler.this.clientChannel.write(buffer,buffer,this);
                }else {
                    if (EchoHandler.this.exit==false){
                        ByteBuffer readbuffer = ByteBuffer.allocate(100);
                        EchoHandler.this.clientChannel.read(readbuffer,readbuffer,new EchoHandler(EchoHandler.this.clientChannel));
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    EchoHandler.this.clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,AIOServerThread>{

    @Override
    public void completed(AsynchronousSocketChannel channel, AIOServerThread aioServerThread) {
        aioServerThread.getServerChannel().accept(aioServerThread,this);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        channel.read(buffer,buffer,new EchoHandler(channel));

    }

    @Override
    public void failed(Throwable exc, AIOServerThread aioServerThread) {
        System.err.print("客户端创建连接失败。。。。");
        aioServerThread.getLatch().countDown();
    }
}

class AIOServerThread implements Runnable{

    private AsynchronousServerSocketChannel serverChannel = null;
    private CountDownLatch latch = null;//做一个同步处理操作
    public AIOServerThread() throws Exception {
        this.latch = new CountDownLatch(1); //等待线程数量为1
        this.serverChannel = AsynchronousServerSocketChannel.open();//打开通道
        this.serverChannel.bind(new InetSocketAddress(HostInfo.PORT));
        System.out.println("服务启动成功，监听端口为：" + HostInfo.PORT);
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    @Override
    public void run() {
        this.serverChannel.accept(this,new AcceptHandler());
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class AIOEchoServer {
    public static void main(String[] args) throws Exception{
        new Thread(new AIOServerThread()).start();
    }
}
