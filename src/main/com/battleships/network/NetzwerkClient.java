package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetzwerkClient extends Network{
    private static final int PORT = 50000;

    private BufferedReader getInputFromHost;
    private BufferedReader sendToHost;




    private PrintWriter out;

    private Socket clientSocket;
    // private String clientAdress;
    // private int clientPort;

    private boolean online = false;


    /**
     *Der Client soll aufgerufen werden mit einer IP Adresse (in Form eines Strings)
     */
    public NetzwerkClient(String adress) throws IOException {
        startClient(adress);
        waitingForInput();
    }

    private void waitingForInput() throws  IOException{
        while(online) {
            String s = getInputFromHost.readLine();
            whatKindOfStringIsThis(1,s);
            getData(s);
            out.println(sendToHost.readLine());
        }
    }

    private void getData(String data){
        System.out.println(data);
    }


    /**
     * startet Client
     */
    private void startClient(String adress) throws IOException {
        //System.out.println("Starting Client...");
        clientSocket = new Socket(adress, PORT);
        online = true;

        getInputFromHost = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sendToHost = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(clientSocket.getOutputStream(),true);

    }

    public static void main(String[] args) {
        try {
            new NetzwerkClient("Localhost");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
