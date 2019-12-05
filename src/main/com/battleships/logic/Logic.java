package com.battleships.logic;

import java.io.*;
import java.util.ArrayList;

public class Logic {
  public static final int HOST = 1;
  public static final int CLIENT = 2;
  public static final int SINGLEPLAYER = 0;
  
  private int playmode;
  private int size;
  
  private Board ownBoard;
  private Board oppBoard;
  
  private boolean isMyTurn;
  
  public static void main(String[] args) {
    new Logic();
  }
  
  public void setPlaymode(int playmode) {
    if(playmode != HOST && playmode != CLIENT && playmode != SINGLEPLAYER) throw new IllegalArgumentException("Playmode has to be " + HOST + " or " + CLIENT + " or " + SINGLEPLAYER + ".");
    this.playmode = playmode;
    switch (playmode){
      case SINGLEPLAYER:
      case HOST:
        isMyTurn = true;
        break;
      case CLIENT:
        isMyTurn = false;
    }
  }
  
  public void setSize(int size) {
    this.size = size;
    ownBoard = new Board(size);
    oppBoard = new Board(size);
  }
  
  public boolean onShoot(int x, int y){
    //TODO
    return false;
  }
  
  public ArrayList<Ship> getShipsByGridSize(int size) {
    ArrayList<Ship> ships = null;
    try {
      ships = new ArrayList<>();
      //FIXME change for Build
      File file = new File("C:\\Users\\lukas\\Desktop\\Schiffeversenken\\src\\main\\resources\\ships.csv");
      BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      
      //Richtige Zeile in der Date finden
      String line = null;
      for (int i = 5; i <= size; i++) line = bf.readLine();
      
      //Anzahlen durchgehen und so viele Schiffe hinzufügen
      String[] elems = line.split(";");
      for (int i = 0; i < elems.length; i++) {
        int shipCount = Integer.parseInt(elems[i]);
        for (int j = 1; j <= shipCount; j++) ships.add(Ship.getDefault(size));
      }
    }catch (IOException e) {
      e.printStackTrace();
    }finally {
      return ships;
    }
  }
}
