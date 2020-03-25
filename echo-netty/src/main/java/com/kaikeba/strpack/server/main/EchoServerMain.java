package com.kaikeba.strpack.server.main;

import com.kaikeba.strpack.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) {
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
