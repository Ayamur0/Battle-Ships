package com.battleships.network;

import java.io.IOException;

public class NetworkManager {

    private Network player;

    public NetworkManager() {
    }

    public boolean start(boolean server, String IP) {
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

    public void sendSize(int size){
        player.sendMessage("size " + size);
    }

    public void sendShoot(int x, int y){
        player.sendMessage("shoot " + (y-1) + " " + (x-1));
        player.setLastShot(x,y);
    }

    public void sendConfirm(){
        player.sendMessage("confirm");
        player.setPlayerConfirm();
    }

    public void sendSave(long ID){
        player.sendMessage("save " + ID);
    }

    public void sendLoad(long ID){
        player.sendMessage("load " + ID);
    }

    public void sendAnswer(int a){
        player.sendMessage("answer " + a);
    }

    public void execute(){
        if(player != null)
            player.execute();
    }
}
