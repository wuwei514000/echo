package com.kaikeba.serialization.client.main;

import com.kaikeba.serialization.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) {
        try {
            new EchoClient().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
