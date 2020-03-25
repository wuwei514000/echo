package com.kaikeba.fastjson.server.main;

import com.kaikeba.fastjson.server.EchoServer;

public class EchoServerMain {
    public static void main(String[] args) {
        try {
            new EchoServer().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
