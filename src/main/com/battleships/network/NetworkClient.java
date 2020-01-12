package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkClient extends Network implements Runnable{
    private static final int PORT = 50000;

    private BufferedReader fromServer;
    private PrintWriter toServer;
    private BufferedReader keyboardInput;




    private PrintWriter out;

    private Socket clientSocket;
    // private String clientAdress;
    // private int clientPort;

    private boolean online = false;


    /**
     *Der Client soll aufgerufen werden mit einer IP Adresse (in Form eines Strings)
     */
    public NetworkClient(String adress){
        try {
            startClient(adress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        toServer.println(message);
        Thread t = new Thread(this);
        t.start();
    }

    public void run(){
        String answer = null;
        try {
            answer = fromServer.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving message from Server!");
        }
        executeStringFunction(1, answer);
    }

    private void pingpong() throws  IOException{
        while(online) {
            String answer = fromServer.readLine();
            executeStringFunction(1,answer);
            printReceivedMessage(answer);
            String send = keyboardInput.readLine();
            toServer.println(send);
        }
    }

    private void printReceivedMessage(String data){
        System.out.println(data);
    }


    /**
     * startet Client
     */
    private void startClient(String adress) throws IOException {
        //System.out.println("Starting Client...");
        clientSocket = new Socket(adress, PORT);
        online = true;
        toServer =  new PrintWriter(clientSocket.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        keyboardInput = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        Thread t = new Thread(this);
        t.start();
    }

    public static void main(String[] args) {
        try {
            new NetworkClient("Localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
