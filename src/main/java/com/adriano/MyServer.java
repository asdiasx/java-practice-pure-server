package com.adriano;


import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;

import static java.net.http.HttpClient.newHttpClient;

public class MyServer {
    public static void main(String[] args) throws Exception {
        var server = HttpServer.create(new InetSocketAddress(8080), 0);
//        server.setExecutor(Executors.newCachedThreadPool());
        var executor = Executors.newFixedThreadPool(10);
        server.setExecutor(executor);

        server.createContext("/path1", exchange -> {
            var msg = "Response from path1: " + exchange.getRequestURI();
            exchange.sendResponseHeaders(200, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        });

        server.createContext("/path2", exchange -> {
            var msg = "Response from path2: " + apiFetchedData();
            exchange.sendResponseHeaders(200, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        });
        server.start();
    }

    private static String apiFetchedData() {
        String apiBody;

        try (var client = newHttpClient()) {

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/path1"))
                    . GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiBody = response.body();
        } catch (Exception e) {
            apiBody = e.getMessage();
            System.out.println(apiBody);
            return apiBody;
        }
        System.out.println(apiBody);
        return apiBody;
    }
}