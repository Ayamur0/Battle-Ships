package com.battleships.logic;

public class GridCell {
  public static final int SHIP = 1;
  public static final int WATER = 0;
  public static final int SHIP_HIT = -1;
  public static final int MISSED_HIT = -2;
  
  private int stat;
  private Ship ship;
  
  public GridCell(int stat) {
    this.stat = stat;
  }
  
  public GridCell(Ship ship){
    this.ship = ship;
    stat = SHIP;
  }
  
  public int getStat() {
    return stat;
  }
  
  public Ship getShip() {
    return ship;
  }
  
  public void setShip(Ship ship) {
    this.ship = ship;
    stat = SHIP;
  }
  
  public boolean fire(){
    if(stat == SHIP){
      ship.hit();
      stat = SHIP_HIT;
      return true;
    }else {
      stat = MISSED_HIT;
      return false;
    }
  }
}
