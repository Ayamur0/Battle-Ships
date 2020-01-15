package com.battleships.network;

/**
 * Interface containing methods each network has to implement.
 *
 * @author Tim Staudenmaier
 */
public interface NetworkInterface {

    /**
     * Sends a message to the opposing server.
     * @param s String to send.
     */
    void sendMessage(String s);

    /**
     * Closes the connection.
     */
    void closeConnection();
}
