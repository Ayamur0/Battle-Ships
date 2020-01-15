package com.battleships.network;

import java.io.IOException;

/**
 * Interface for communicating with the network.
 * Used by logic and gui.
 *
 * @author Tim Staudenmaier
 */
public class NetworkManager {

    /**
     * Network class of the player,
     */
    private Network player;
    /**
     * {@code true} if the player is the server, {@code false} if the player is the client.
     */
    private boolean server;
    /**
     * {@code true} if the player has already confirmed ship placement but the confirm couldn't be sent
     * because network was waiting for opponent confirm.
     */
    private boolean confirmCanBeSent;

    /**
     * Start a new network.
     * @param server {@code true} if the network should be a server, {@code false} if it should be a client.
     * @param IP IP this network should connect to (only is it is client, else this can be null).
     * @return {@code true} if the network could be created, {@code false} if client couldn't connect to server or server
     * couldn't be created.
     */
    public boolean start(boolean server, String IP) {
        this.server = server;
        if(server) {
            player = new NetworkServer();
        }
        else {
            try {
                player = new NetworkClient(IP);
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Send a pass message to the opposing network.
     */
    public void sendPass(){
        if(player != null){
            player.sendMessage("pass");
        }
    }

    /**
     * Send the size of the grid to the opposing network.
     */
    public void sendSize(int size){
        if(player != null)
            player.sendMessage("size " + size);
    }

    /**
     * Send a shoot command to the opposing network.
     * @param x X index of the shoot command to be sent (1-size).
     * @param y Y index of the shoot command to be sent (1-size).
     */
    public void sendShoot(int x, int y){
        if(player != null){
            player.sendMessage("shot " + (y-1) + " " + (x-1));
            player.setLastShot(x,y);
        }
    }

    /**
     * Send a confirm message to the opposing network.
     */
    public void sendConfirm(){
        if(server && !confirmCanBeSent) {
            confirmCanBeSent = true;
            return;
        }
        confirmCanBeSent = false;
        if(player != null) {
            player.sendMessage("confirmed");
            player.setPlayerConfirm();
        }
    }

    /**
     * Send a saved command to the opposing network.
     * @param ID ID the game should be saved with.
     */
    public void sendSave(String ID){
        if(player != null)
            player.sendMessage("save " + ID);
    }

    /**
     * Send a load message to the opposing network.
     * @param ID ID for the game that should be loaded.
     */
    public void sendLoad(String ID){
        if(player != null)
            player.sendMessage("load " + ID);
    }

    /**
     * Send an answer to a shot to the opposing network.
     * @param a What answer to send (0 for water, 1 for ship hit, 2 for hit and sunk)
     */
    public void sendAnswer(int a){
        if(player != null)
            player.sendMessage("answer " + a);
    }

    /**
     * Execute the last received command.
     */
    public void execute(){
        if(player != null)
            player.execute();
    }

    /**
     * Stop searching for a connection to a client. (If player is server)
     */
    public void stopConnectionSearch(){
        if(player instanceof NetworkServer)
            ((NetworkServer) player).stopConnectionSearch();
        closeConnection();
    }

    /**
     * Close the current connection.
     */
    public void closeConnection(){
        if(player != null)
            player.closeConnection();
    }

    /**
     * @return {@code true} if the confirm of the player is ready to be sent.
     */
    public boolean isConfirmCanBeSent() {
        return confirmCanBeSent;
    }

    /**
     * @return {@code true} if the player has confirmed ship placement.
     */
    public boolean hasPlayerConfirmed(){
        if(player == null)
            return false;
        return player.isPlayerConfirm() || confirmCanBeSent;
    }
}
