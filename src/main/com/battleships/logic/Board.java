package com.battleships.logic;

import static com.battleships.logic.Ship.HORIZONTAL;
import static com.battleships.logic.GridCell.WATER;
import static com.battleships.logic.Ship.VERTICAL;

public class Board {
  private GridCell[][] board;
  private int shipCount;
  
  public Board(int size) throws IllegalArgumentException {
    if (size > 30)
      throw new IllegalArgumentException(String.format("%s is not permitted. Size has to be between 5 and 30", size));
    
    board = new GridCell[size][size];
    for (int i = 0; i < board.length; i++) for (int j = 0; j < board[i].length; j++) board[i][j] = new GridCell(WATER);
  }
  
  public boolean placeShip(Ship ship) {
    for (int i = 0; i < ship.getSize(); i++) {
      if (ship.getDir() == HORIZONTAL) {
        board[ship.getY()][ship.getX() + i].setShip(ship);
      }else if (ship.getDir() == VERTICAL) {
        board[ship.getY() + i][ship.getX()].setShip(ship);
      }
    }
    
    shipCount++;
    return true;
  }
  
  public boolean fire(int x, int y) {
    return board[y][x].fire();
  }
  
  public boolean shipsLeft() {
    return shipCount > 0;
  }
  
  public void render(){
    for (GridCell[] row : board) {
      System.out.print("\n");
      for (GridCell cell : row) {
        System.out.print(cell.getStat() + " ");
      }
    }
  }
  
  
  //TODO shipCount immer verringern
}
