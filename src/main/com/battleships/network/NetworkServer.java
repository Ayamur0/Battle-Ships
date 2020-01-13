package com.battleships.network;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenu;
import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.MainMenuGui.WaitingConnection;
import com.battleships.gui.gameAssets.grids.GridManager;
import com.battleships.logic.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkServer extends Network implements Runnable{
    //In der Vorlesung ausgemachter Port den alle verwenden
    public static final int PORT = 50000;

    private PrintWriter toClient;
    private BufferedReader keyboardInput;
    private BufferedReader fromClient;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private connect waitingForConnection;
    private boolean connected;

    private Thread connectionSearch;

    private boolean waitingForMessage;

    //Konstruktor für den Server
    public NetworkServer(){
        startServer();
        waitingForConnection = new connect(this);
        connectionSearch = new Thread(waitingForConnection);
        connectionSearch.start();
    }

    private static class connect implements Runnable{
        private NetworkServer server;
        private volatile boolean isRunning = true;

        public connect(NetworkServer server) {
            this.server = server;
        }

        public void kill(){
            isRunning = false;
            server.connected = true;
        }

        @Override
        public void run() {
            server.waitForClient();
        }
    }

    public void stopConnectionSearch(){
//        waitingForConnection.kill();
//        waitingForConnection = new connect(this);
        connectionSearch.interrupt();
    }

    public void sendMessage(String message){
        if(waitingForMessage)
            return;
        toClient.println(message);
        System.out.println("\u001B[32m" + "Sent: " + message);
        System.out.println("\u001B[31m" + "now waiting");
        waitingForMessage = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void run(){
        String answer = null;
        try {
            answer = fromClient.readLine();
            System.out.println("\u001B[0m" + answer);
        } catch (IOException e) {
            System.err.println("Error receiving message from Client!");
        }
        waitingForMessage = false;
        System.out.println("\u001B[31m" + "stopped waiting");
        setStringFunction(1, answer);
    }

    /**
     * Die while schleife lässt den Server immer etwas schreiben und wartet dann bis der Client etwas bekommt
     * @throws IOException
     */
    private void pingong() throws IOException{
        while(true) {
            toClient.println(keyboardInput.readLine());
            String answer = fromClient.readLine();
            System.out.println(answer);
            setStringFunction(0, answer);
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
            keyboardInput = new BufferedReader(new InputStreamReader(System.in));
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
            System.out.println("Waiting for Client");
            clientSocket = serverSocket.accept();
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connected!");
            if(MainMenuManager.getMenu() instanceof WaitingConnection){
                ((WaitingConnection) MainMenuManager.getMenu()).setOpponentConnected(true);
            }
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
        new NetworkServer();
    }

    public boolean isConnected() {
        return connected;
    }

    public void closeConnection(){
        try {
            serverSocket.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
