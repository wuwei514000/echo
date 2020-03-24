package com.kaikeba.netty.server.main;

import com.kaikeba.netty.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) {
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
