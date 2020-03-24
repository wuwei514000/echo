package com.kaikeba.bio.client;

import com.kaikeb.util.InputUtil;
import com.kaikeba.info.HostInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class BIOEchoClient {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket(HostInfo.HOST_NAME,HostInfo.PORT);//定义连接的主机信息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));//获取服务器响应的数据
        PrintStream out = new PrintStream(client.getOutputStream());//性服务端发送信息内容
        boolean flag = true;
        while (flag){
            String inputData = InputUtil.getString("请输入要发送的内容： ");
            out.print(inputData + "\n");//数据发送到服务器上
            System.out.println(in.readLine());
            if ("byebye".equalsIgnoreCase(inputData)){
                flag = false;
            }
        }
        in.close();
        out.close();
        client.close();
    }
}
