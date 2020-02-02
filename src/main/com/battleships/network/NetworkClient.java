package com.battleships.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Network implementation for the client side.
 *
 * @author Tim Staudenmaier
 */
public class NetworkClient extends Network implements Runnable {
    /**
     * Port this network uses.
     */
    private static final int PORT = 50000;

    /**
     * Reader to read the messages the server sends to this client.
     */
    private BufferedReader fromServer;
    /**
     * Writer to send messages to the server.
     */
    private PrintWriter toServer;

    /**
     * {@code true} while this network is waiting for a message from the server.
     * {@code false} while this network is able to send a message to the server.
     */
    private boolean waitingForMessage;

    /**
     * Socket this client uses.
     */
    private Socket clientSocket;

    /**
     * Start a client that tries to connect to the server with the passed IP.
     * @param adress IP-Address of the server this client should connect to.
     */
    public NetworkClient(String adress) {
        try {
            startClient(adress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends a message to the server.
     *
     * @param message Message to send.
     */
    public void sendMessage(String message) {
        if (!message.contains("save") && waitingForMessage)
            return;
        toServer.println(message);
        System.out.println("\u001B[32m" + "Sent: " + message);
        System.out.println("\u001B[31m" + "now waiting");
        waitingForMessage = true;
    }

    /**
     * Waits for an answer from the server.
     */
    public void run() {
        while(true) {
            String answer;
            try {
                answer = fromServer.readLine();
                if (answer == null) {
                    setStringFunction(null);
                    break;
                }
                if (!answer.contains("save") && !waitingForMessage) {
                    continue;
                }
                System.out.println("\u001B[0m" + answer);
            } catch (IOException e) {
                System.err.println("Error receiving message from Server!");
                break;
            }
            waitingForMessage = false;
            System.out.println("\u001B[31m" + "stopped waiting");
            setStringFunction(answer);
        }
    }

    /**
     * Start client and initialize all needed readers.
     * @param adress IP-Address of the server this client should connect to.
     * @throws IOException If the readers couldn't write
     */
    private void startClient(String adress) throws IOException {
        //System.out.println("Starting Client...");
        clientSocket = new Socket(adress, PORT);
        toServer = new PrintWriter(clientSocket.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        waitingForMessage = true;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Close the connection of this client.
     */
    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
