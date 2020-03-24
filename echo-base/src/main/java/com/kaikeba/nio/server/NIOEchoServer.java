package com.kaikeba.nio.server;

import com.kaikeba.info.HostInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOEchoServer {

    private static class EchoClientHandler implements Runnable{
        private SocketChannel clientChannel;
        private boolean flag = true;

        public EchoClientHandler(SocketChannel clientChannel) {
            this.clientChannel = clientChannel;
            //严格意义上来说，当已经连接上服务器，并且需要进一步处理之前发送给客户端

        }
        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(50);//buffer缓存区
            try {
                //读取数据发送数据
                while (this.flag){
                    buffer.clear();
                    int readCount = this.clientChannel.read(buffer);//向缓存区读取数据
                    String readMessage = new String(buffer.array(), 0, readCount).trim();
                    System.out.println("【ECHO客户端：】" + readMessage);
                    String writeMessage = "[ECHO]:" + readMessage +"\n";//回应响应数据
                    if ("byebye".equalsIgnoreCase(readMessage)){
                        writeMessage = "【EXIT】 拜拜下次再见";
                        this.flag =false;
                    }
                    //发送数据
                    buffer.clear();
                    buffer.put(writeMessage.getBytes());
                    buffer.flip();;
                    this.clientChannel.write(buffer);
                }
                this.clientChannel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //考略到性能，开启线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //需要设置一个非阻塞状态的机制
        serverSocketChannel.configureBlocking(false);//非阻塞模式

        //服务器上需要提供一个网络监听的端口
        serverSocketChannel.bind(new InetSocketAddress(HostInfo.PORT));

        //设置一个selector作为服务器出现，目的是管理所有的channel

        Selector selector = Selector.open();
        //将当前的channel注册到selector中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//连接时处理

        System.out.println("服务已经启动，监听端口为： " + HostInfo.PORT);

        //NIO采用轮询模式，每当发现有用户连接的时候需要启动一个线程池（线程池管理）
        int keySelect = 0;
        while((keySelect = selector.select()) > 0){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();
                if (selectionKey.isAcceptable()){
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    if (clientChannel != null){
                        executorService.submit(new EchoClientHandler(clientChannel));
                    }
                }
            }
        }
        executorService.shutdown();
        serverSocketChannel.close();
    }
}
