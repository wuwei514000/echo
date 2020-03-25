package com.kaikeba.strpack.client.main;

import com.kaikeba.strpack.client.EchoClient;

public class EchoClientMain {
    public static void main(String[] args) {
        try {
            new EchoClient().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
