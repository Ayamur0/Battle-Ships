package com.battleships.network;

public class NetworkManager {

    private Network player;

    public NetworkManager() {
    }

    public void start(boolean server) {
        if(server)
            player = new NetworkServer();
        else
            player = new NetworkClient("Localhost");

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

    public void sendSave(int ID){
        player.sendMessage("save " + ID);
    }

    public void sendLoad(int ID){
        player.sendMessage("load " + ID);
    }
}
