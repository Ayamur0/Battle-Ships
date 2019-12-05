package com.battleships.logic;

public class Ship {
  //TODO entityIndex hinzufügen
  //TODO implement dir with 4 options
  public static final int HORIZONTAL = 1;
  public static final int VERTICAL = -1;
  
  private final String[] types = {"U-Boot", "Zerstörer", "Schlachtschiff", "Flugzeugträger"};
  
  private String type;
  private int size;
  private int dir;
  private int x;
  private int y;
  private int hits;
  private boolean alive;
  
  public Ship(int size, int dir, int x, int y) {
    this.size = size;
    this.dir = dir;
    this.x = x;
    this.y = y;
    
    type = types[size - 2];
    alive = true;
  }
  
  public static Ship getDefault(int size){
    return new Ship(size, VERTICAL, 0, 0);
  }
  
  public void setDir(int dir) {
    this.dir = dir;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public void setSize(int size) {
    this.size = size;
    type = types[size - 2];
  }
  
  public void hit(){
    hits++;
    if(hits >= size) alive = false;
  }
  
  public int getSize() {
    return size;
  }
  
  public int getDir() {
    return dir;
  }
  
  public int getX() {
    return x;
  }
  
  public int getY() {
    return y;
  }
  
  public int getHits() {
    return hits;
  }
  
  public boolean isAlive() {
    return alive;
  }
  
  @Override
  public String toString() {
    return type;
  }
}