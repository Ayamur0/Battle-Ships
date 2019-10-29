package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetzwerkServer {
    public static final int PORT = 6666;

    private PrintWriter toClient;
    private BufferedReader sendClient;
    private BufferedReader fromClient;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    public NetzwerkServer(){
        startServer();
        waitForClient();
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendData() throws IOException{
        while(true) {
            toClient.println(sendClient.readLine());
            System.out.println(fromClient.readLine());
        }
    }

    private void startServer(){
        try {
            System.out.println("Startin Server");
            serverSocket = new ServerSocket(PORT);
            sendClient = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForClient() {
        try {
            System.out.println("Waitin for Client");
            clientSocket = serverSocket.accept();
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connected!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new NetzwerkServer();
    }
}
