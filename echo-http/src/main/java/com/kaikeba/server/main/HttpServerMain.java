package com.kaikeba.server.main;

import com.kaikeba.server.HTTPServer;

public class HttpServerMain {

    public static void main(String[] args) throws Exception {
        new HTTPServer().run();
    }
}
