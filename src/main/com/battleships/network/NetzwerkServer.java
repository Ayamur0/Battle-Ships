package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetzwerkServer {
    //In der Vorlesung ausgemachter Port den alle verwenden
    public static final int PORT = 50000;

    private PrintWriter toClient;
    private BufferedReader sendClient;
    private BufferedReader fromClient;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private String shot = "shoot";
    private String start = "start";
    private String confirmed = "confirmed";

    //Konstruktor für den Server
    public NetzwerkServer(){
        startServer();
        waitForClient();
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //TBH will ich des nicht so machen aber bleibt mir eine andere Möglichkeit??? Send Help pls

    //Die Methode bekommt den Befehl übergeben, was der gegenüber den gemacht hat und schreibt das jeweilige
    //befehlswort "shot", "start", "confirmed" aus dem Befehlsstring raus und übrig sollten die Koordinaten beliben

    //TODO Was mach ich mit den Koordinaten??
    private void whatKindOfStringIsThis(String text){
        if(text.contains(shot)){
            text = text.replace(shot, "");
        }else if(text.contains(start)){
            text = text.replace(start, "");
        }else if(text.contains(confirmed)){
            text = text.replace(confirmed, "");
        }else if(false);
    }


    private void sendData() throws IOException{
        while(true) {
            toClient.println(sendClient.readLine());
            whatKindOfStringIsThis(fromClient.readLine());
            //System.out.println(fromClient.readLine());
        }
    }
    //Starten den Server mit dem dazugehörigen Port
    private void startServer(){
        try {
            System.out.println("Startin Server");
            serverSocket = new ServerSocket(PORT);

            //Der BufferedReader wird dafür verwendet den Input von der Logic dem Client zu senden.
            sendClient = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wartet auf den Client bis der sich mit dem Server verbunden hat
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

    //Main
    public static void main(String[] args) {
        new NetzwerkServer();
    }
}
