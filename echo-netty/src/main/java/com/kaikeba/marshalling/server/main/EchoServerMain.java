package com.kaikeba.marshalling.server.main;

import com.kaikeba.marshalling.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) {
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
