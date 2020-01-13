package com.battleships.network;

import java.io.IOException;

public class NetworkManager {

    private Network player;
    private boolean server;
    private boolean confirmCanBeSent;

    public NetworkManager() {
    }

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

    public void sendPass(){
        if(player != null){
            player.sendMessage("pass");
        }
    }

    public void sendSize(int size){
        if(player != null)
            player.sendMessage("size " + size);
    }

    public void sendShoot(int x, int y){
        if(player != null){
            player.sendMessage("shot " + (y-1) + " " + (x-1));
            player.setLastShot(x,y);
        }
    }

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

    public void sendSave(String ID){
        if(player != null)
            player.sendMessage("save " + ID);
    }

    public void sendLoad(String ID){
        if(player != null)
            player.sendMessage("load " + ID);
    }

    public void sendAnswer(int a){
        if(player != null)
            player.sendMessage("answer " + a);
    }

    public void execute(){
        if(player != null)
            player.execute();
    }

    public boolean isServerConnected(){
        if(player instanceof NetworkServer)
            return ((NetworkServer) player).isConnected();
        else
            return false;
    }

    public void stopConnectionSearch(){
        if(player instanceof NetworkServer)
            ((NetworkServer) player).stopConnectionSearch();
    }

    public void closeConnection(){
        if(player != null)
            player.closeConnection();
    }

    public boolean isConfirmCanBeSent() {
        return confirmCanBeSent;
    }
}
