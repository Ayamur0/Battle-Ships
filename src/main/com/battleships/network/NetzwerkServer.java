package com.battleships.network;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Settings;
import org.joml.Vector2i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetzwerkServer extends Network {
    //In der Vorlesung ausgemachter Port den alle verwenden
    public static final int PORT = 50000;

    private PrintWriter toClient;
    private BufferedReader sendClient;
    private BufferedReader fromClient;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private String shot = "shoot ";
    private String size = "size ";
    private String confirmed = "confirmed ";

    private static GameManager gameManager;
    private static GridManager gridManager;

    private static Settings settings;
    //private final Logic logic;

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

    /**
     * Die while schleife lässt den Server immer etwas schreiben und wartet dann bis der Client etwas bekommt
     * @throws IOException
     */
    private void sendData() throws IOException{
        while(true) {
            toClient.println(sendClient.readLine());
            whatKindOfStringIsThis(0, fromClient.readLine());
            //System.out.println(fromClient.readLine());
        }
    }

    /**
     * Startet den Server mit dem entsprechenden Port, sowie einen BufferedReader um einen Input zu schicken
     */
    private void startServer(){
        try {
            //System.out.println("Startin Server");
            serverSocket = new ServerSocket(PORT);
            //Der BufferedReader wird dafür verwendet den Input von der Logic dem Client zu senden.
            sendClient = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //wartet auf den Client bis der sich mit dem Server verbunden hat

    /**
     * Die Methode wird ausgeführt nachdem der Server gestartet wurde. Sie wartet bis der Client sich verbindet
     * und erstellt einen Reader und Writer für die Kommunikation zwischen Server und Client.
     */
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
    public void shoot(int x, int y){
        String s = "shoot " + x + " " + y;
        toClient.println(s);
    }

    //Main
    public static void main(String[] args) {
        new NetzwerkServer();
    }
}
