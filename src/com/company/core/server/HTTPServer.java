package com.company.core.server;

import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    private static Socket request;

    // Constructor
    private HTTPServer(Socket request) {
        this.request = request;
    }

    public Socket getRequest() {
        return request;
    }

    // Method to initialize server
    public static void init(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SYSTEM] Server is ready at port " + port);
            while(true) {
                // Create new accept
                HTTPServer server = new HTTPServer(serverSocket.accept());
                // Create a new thread by creating a new ClientHandler
                Thread thread = new ClientHandler(server);
                // Start thread
                thread.start();
            }
        } catch (Exception e) {
            System.out.println("[INIT] Server has closed connection");
        }
    }
}
