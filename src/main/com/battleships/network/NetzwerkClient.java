package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetzwerkClient {

    private static final int PORT = 50000;

    private BufferedReader getInputFromHost;
    private BufferedReader sendToHost;

    private PrintWriter out;

    private Socket clientSocket;
    // private String clientAdress;
    // private int clientPort;

    public NetzwerkClient(String adress, int port) throws IOException {

        startClient(adress,port);
        waitinForImput();
    }

    private void waitinForImput() throws  IOException{
        while(true) {
            getData(getInputFromHost.readLine());
            out.println(sendToHost.readLine());
        }
    }

    public void getData(String data){
        System.out.println(data);
    }

    private void startClient(String adress, int port) throws IOException {
        System.out.println("Startin Client...");
        clientSocket = new Socket(adress, port);
        getInputFromHost = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sendToHost = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public static void main(String[] args) {
        try {
            new NetzwerkClient("Localhost", PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
