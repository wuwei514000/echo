package com.kaikeba.messagepack.client.main;

import com.kaikeba.messagepack.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) {
        try {
            new EchoClient().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
