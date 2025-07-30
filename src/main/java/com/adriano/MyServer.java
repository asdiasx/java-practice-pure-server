package com.adriano;


import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class MyServer {
    public static void main(String[] args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/path1", exchange -> {
            var msg = "Response from path1: " + exchange.getRequestURI();
            exchange.sendResponseHeaders(200, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        });
        server.start();
    }
}