package com.kaikeba.bio.server;

import com.kaikeba.info.HostInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOEchoServer {

    public static void main(String[] args) throws IOException {
        ServerSocket sc = new ServerSocket(HostInfo.PORT);
        System.out.println("服务端已经启动。监听端口：" + HostInfo.PORT);
        boolean flag = true;
        //设置线程池的大小
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while(flag){
            Socket client = sc.accept();
            executorService.submit(new EchoClientHandle(client));
        }
        executorService.shutdown();
        sc.close();
    }


    private static class EchoClientHandle implements  Runnable{
        private Socket client;  //每一个用户都需要启动一个任务来执行
        private BufferedReader in;
        private PrintStream out;
        private boolean flag = true;
        public EchoClientHandle(Socket client) {
            this.client = client;
            try {
                this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                this.out = new PrintStream(this.client.getOutputStream(),true);
            } catch (IOException e) {
            }
        }

        public void run() {
            while (this.flag){
                try {
                    String readMessage = in.readLine();
                    System.out.println("【客户端请求来了】：" + readMessage);
                    String writeMessage = null;
                    if("byebye".equalsIgnoreCase(readMessage)){
                        this.flag=false;
                        writeMessage = "【拜拜，下次再见】";
                    }else{
                        writeMessage = "【ECHO】:" + readMessage + "\n";
                    }
                    out.print(writeMessage);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                this.in.close();
                this.out.close();
                this.client.close();
            } catch (IOException e) {
            }
        }
    }
}
