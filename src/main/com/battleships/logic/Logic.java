package com.praktikum.logic;

import java.io.*;
import java.util.ArrayList;

import static com.praktikum.logic.Ship.*;

public class Logic {
  private final int TEST_SIZE = 10;
  
  Board ownBoard;
  Board oppBoard;
  
  public static void main(String[] args) {
    new Logic();
  }
  
  public Logic() {
    ownBoard = new Board(TEST_SIZE);
    oppBoard = new Board(TEST_SIZE);
  
    for (Ship ship : testShipPlacement()) ownBoard.placeShip(ship);
    ownBoard.render();
    
    while (ownBoard.shipsLeft() && oppBoard.shipsLeft()) {
      //TODO
    }
  }
  
  private ArrayList<Ship> getShipsByGridSize(int size) {
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
  
  //<editor-fold desc="Test Methods">
  private ArrayList<Ship> testShipPlacement() {
    return new ArrayList<Ship>() {{
      add(new Ship(3, HORIZONTAL, 0, 0));
      add(new Ship(5, VERTICAL, 3, 2));
      add(new Ship(4, HORIZONTAL, 5, 9));
      add(new Ship(2, VERTICAL, 9, 3));
    }};
  }
  //</editor-fold>
}
