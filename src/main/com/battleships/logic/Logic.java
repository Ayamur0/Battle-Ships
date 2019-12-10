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
  
  public Logic() {
    getShipsByGridSize(30);
  }
  
  public void setPlaymode(int playmode) {
    if (playmode != HOST && playmode != CLIENT && playmode != SINGLEPLAYER)
      throw new IllegalArgumentException("Playmode has to be " + HOST + " or " + CLIENT + " or " + SINGLEPLAYER + ".");
    this.playmode = playmode;
    switch (playmode) {
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
  
  public boolean onShoot(int x, int y) {
    //TODO
    return false;
  }
  
  public ArrayList<Ship> getShipsByGridSize(int size) {
    if (size < 5 || size > 30) throw new IllegalArgumentException("Size has to be between 5 and 30!");
    
    ArrayList<Ship> ships = null;
    
    try {
      ships = new ArrayList<>();
      File file = new File(getClass().getResource("/schiffstabelle.csv").toURI());
      BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
      
      String line = null;
      for (int i = 4; i < size; i++) {
        line = bf.readLine();
      }
      
      String shipCounts[] = line.split(";");
      for (int i = shipCounts.length - 1; i >= 0; i--) {
        int shipCount = Integer.parseInt(shipCounts[i]);
        for (int j = 0; j < shipCount; j++) {
          ships.add(Ship.getDefault(i + 2));
        }
      }
    }catch (Exception e) {
      e.printStackTrace();
      System.err.println("Reading 'Schiffstabelle.csv' was not possible :(");
    }finally {
      return ships;
    }
  }
  
  public boolean placeShip(Ship ship){
    return ownBoard.placeShip(ship);
  }
}
