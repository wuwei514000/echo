package com.kaikeba.pack.server.main;

import com.kaikeba.pack.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) {
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
