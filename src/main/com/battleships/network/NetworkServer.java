package com.battleships.network;

import com.battleships.gui.gameAssets.MainMenuGui.MainMenuManager;
import com.battleships.gui.gameAssets.MainMenuGui.WaitingConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetworkServer extends Network implements Runnable {
    /**
     * Port this network uses.
     */
    public static final int PORT = 50000;

    /**
     * Writer to write messages to client.
     */
    private PrintWriter toClient;
    /**
     * Reader to read messages from client.
     */
    private BufferedReader fromClient;

    /**
     * Socket of this server.
     */
    private ServerSocket serverSocket;
    /**
     * Socket of the client this server is connected to.
     */
    private Socket clientSocket;

    /**
     * Thread in which the server can search for a connection to a client.
     */
    private Thread connectionSearch;

    /**
     * {@code true} while this network is waiting for a message from the client.
     * {@code false} while this network is able to send a message to the client.
     */
    private boolean waitingForMessage;

    /**
     * Start a server that searches for a client to connect with.
     */
    public NetworkServer() {
        startServer();
        connect waitingForConnection = new connect(this);
        connectionSearch = new Thread(waitingForConnection);
        connectionSearch.start();
    }

    /**
     * Stops searching for a connection.
     */
    public void stopConnectionSearch() {
        connectionSearch.interrupt();
    }

    /**
     * Sends a message to the client.
     *
     * @param message Message to send.
     */
    public void sendMessage(String message) {
        if (!message.contains("save") && waitingForMessage)
            return;
        toClient.println(message);
        System.out.println("\u001B[32m" + "Sent: " + message);
        System.out.println("\u001B[31m" + "now waiting");
        waitingForMessage = true;
    }

    /**
     * Waits for an answer from the client.
     */
    public void run() {
        while(true) {
            String answer;
            try {
                answer = fromClient.readLine();
                if(answer == null) {
                    setStringFunction(null);
                    break;
                }
                if(!answer.contains("save") && !waitingForMessage) {
                    continue;
                }
                System.out.println("\u001B[0m" + answer);
            } catch (IOException e) {
                System.err.println("Error receiving message from Client!");
                break;
            }
            waitingForMessage = false;
            System.out.println("\u001B[31m" + "stopped waiting");
            setStringFunction(answer);
        }
    }

    /**
     * Starts the server.
     */
    private void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait for a client to connect to this server. Should be executed in different thread by using {@link connect} class.
     *
     * @throws IOException If the socket accept gets interrupted (by close connection).
     */
    private void waitForClient() throws IOException {
        System.out.println("Waiting for Client");
        clientSocket = serverSocket.accept();
        try {
            toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connected!");
        if (MainMenuManager.getMenu() instanceof WaitingConnection) {
            ((WaitingConnection) MainMenuManager.getMenu()).setOpponentConnected(true);
        }
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Close the connection of this server.
     */
    public void closeConnection() {
        try {
            if (serverSocket != null)
                serverSocket.close();
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private class to run the connection search in different thread.
     */
    private static class connect implements Runnable {
        /**
         * Network for which this class searches a connection.
         */
        private NetworkServer server;

        /**
         * Create a new class to search a connection.
         *
         * @param server Network for which connection is needed.
         */
        public connect(NetworkServer server) {
            this.server = server;
        }

        /**
         * Method that searches the connection.
         * Execute in different Thread.
         */
        @Override
        public void run() {
            try {
                server.waitForClient();
            } catch (SocketException ignore) {
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
