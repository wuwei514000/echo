package com.kaikeba.marshalling.client.main;

import com.kaikeba.marshalling.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) {
        try {
            new EchoClient().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
