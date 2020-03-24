package com.kaikeba.aio.client;

import com.kaikeb.util.InputUtil;
import com.kaikeba.info.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

class ReadClientHnadler implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public ReadClientHnadler(AsynchronousSocketChannel clientChannel,CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }
    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        String readMessage = new String(buffer.array(),0,buffer.remaining());
        System.out.println(readMessage);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.latch.countDown();
    }
}
class ClientWriteHandle implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel clientChannel;
    private CountDownLatch latch;

    public ClientWriteHandle(AsynchronousSocketChannel clientChannel,CountDownLatch latch) {
        this.clientChannel = clientChannel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {

        if (buffer.hasRemaining()){
            this.clientChannel.write(buffer,buffer,this);
        }else{
            ByteBuffer readBuffer = ByteBuffer.allocate(100);
            this.clientChannel.read(readBuffer,readBuffer,new ReadClientHnadler(this.clientChannel,this.latch));
        }
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


class AIOClientThread implements Runnable{

    private AsynchronousSocketChannel clientCahnnel;
    private CountDownLatch latch;

    public AIOClientThread() {
        try {
            this.clientCahnnel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.clientCahnnel.connect(new InetSocketAddress(HostInfo.HOST_NAME,HostInfo.PORT));
        this.latch = new CountDownLatch(1);
    }

    @Override
    public void run() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean sendMessage(String msg){
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put(msg.getBytes());
        buffer.flip();
        this.clientCahnnel.write(buffer,buffer,new ClientWriteHandle(this.clientCahnnel,this.latch));
        if ("byebye".equalsIgnoreCase(msg)){
            return false;
        }
        return true;
    }
}

public class AIOEchoClient {
    public static void main(String[] args) throws Exception{
        AIOClientThread client = new AIOClientThread();
        new Thread(client).start();
        while (client.sendMessage(InputUtil.getString("请输入要发送的内容："))){
            ;
        }
    }
}
