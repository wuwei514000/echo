package com.kaikeba.netty.client.main;

import com.kaikeba.netty.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) {
        try {
            new EchoClient().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
