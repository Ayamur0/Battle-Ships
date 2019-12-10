package com.battleships.logic;

import static com.battleships.logic.GridCell.SHIP;
import static com.battleships.logic.GridCell.WATER;
import static com.battleships.logic.Ship.EAST;
import static com.battleships.logic.Ship.NORTH;
import static com.battleships.logic.Ship.SOUTH;
import static com.battleships.logic.Ship.WEST;

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
    if (!canShipBePlaced(ship)) return false;
    
    for (int i = 0; i < ship.getSize(); i++) {
      switch (ship.getDir()) {
        case NORTH:
          board[ship.getY() + i][ship.getX()].setShip(ship);
          break;
        case SOUTH:
          board[ship.getY() - i][ship.getX()].setShip(ship);
          break;
        case WEST:
          board[ship.getY()][ship.getX() + i].setShip(ship);
          break;
        case EAST:
          board[ship.getY()][ship.getX() - i].setShip(ship);
          break;
      }
    }
    
    shipCount++;
    return true;
  }
  
  private boolean canShipBePlaced(Ship ship) {
    switch (ship.getDir()) {
      case NORTH:
        if (ship.getY() >= board.length || ship.getY() + ship.getSize() < 0) return false;
        if (ship.getX() >= board.length || ship.getX() < 0) return false;
        
        if (ship.getY() - 1 >= 0 && board[ship.getY() - 1][ship.getX()].getStat() == SHIP) return false;
        if (ship.getY() + ship.getSize() < board.length && board[ship.getY() + ship.getSize()][ship.getX()].getStat() == SHIP) return false;
        break;
      case SOUTH:
        if (ship.getY() - ship.getSize() >= board.length || ship.getY() < 0) return false;
        if (ship.getX() >= board.length || ship.getX() < 0) return false;
        
        if (ship.getY() - ship.getSize() >= 0 && board[ship.getY() - ship.getSize()][ship.getX()].getStat() == SHIP) return false;
        if (ship.getY() + 1 < board.length && board[ship.getY() + 1][ship.getX()].getStat() == SHIP) return false;
        break;
      case WEST:
        if (ship.getY() >= board.length || ship.getY() < 0) return false;
        if (ship.getX() + ship.getSize() >= board.length || ship.getX() < 0) return false;
  
        if (ship.getX() - 1 >= 0 && board[ship.getY()][ship.getX() - 1].getStat() == SHIP) return false;
        if (ship.getX() + ship.getSize() < board[0].length && board[ship.getY()][ship.getX() + ship.getSize()].getStat() == SHIP) return false;
        break;
      case EAST:
        if (ship.getY() >= board.length || ship.getY() < 0) return false;
        if (ship.getX() >= board.length || ship.getX() - ship.getSize() < 0) return false;
  
        if (ship.getX() + 1 >= 0 && board[ship.getY()][ship.getX() + 1].getStat() == SHIP) return false;
        if (ship.getX() - ship.getSize() < board[0].length && board[ship.getY()][ship.getX() - ship.getSize()].getStat() == SHIP) return false;
        break;
    }
    
    for (int i = 0; i < ship.getSize(); i++) {
      switch (ship.getDir()) {
        case NORTH:
          if (board[ship.getY() + i][ship.getX()].getStat() == SHIP) return false;
          if (ship.getX() - 1 >= 0 && board[ship.getY() + i][ship.getX() - 1].getStat() == SHIP) return false;
          if (ship.getX() + 1 < board[0].length && board[ship.getY() + i][ship.getX() + 1].getStat() == SHIP) return false;
          break;
        case SOUTH:
          if (board[ship.getY() - i][ship.getX()].getStat() == SHIP) return false;
          if (ship.getX() - 1 >= 0 && board[ship.getY() - i][ship.getX() - 1].getStat() == SHIP) return false;
          if (ship.getX() + 1 < board[0].length && board[ship.getY() - i][ship.getX() + 1].getStat() == SHIP) return false;
          break;
        case WEST:
          if (board[ship.getY()][ship.getX() + i].getStat() == SHIP) return false;
          if (ship.getY() - 1 >= 0 && board[ship.getY() - 1][ship.getX() + i].getStat() == SHIP) return false;
          if (ship.getY() + 1 < board.length && board[ship.getY() + 1][ship.getX() + i].getStat() == SHIP) return false;
          break;
        case EAST:
          if (board[ship.getY()][ship.getX() - i].getStat() == SHIP) return false;
          if (ship.getY() - 1 >= 0 && board[ship.getY() - 1][ship.getX() - i].getStat() == SHIP) return false;
          if (ship.getY() + 1 < board.length && board[ship.getY() + 1][ship.getX() - i].getStat() == SHIP) return false;
          break;
      }
    }
    
    return true;
  }
  
  public boolean fire(int x, int y) {
    return board[y][x].fire();
  }
  
  public boolean shipsLeft() {
    return shipCount > 0;
  }
  
  public void dumpBoard() {
    for (GridCell[] row : board) {
      System.out.print("\n");
      for (GridCell cell : row) {
        System.out.print(cell.getStat() + " ");
      }
    }
  }
  
  
  //TODO shipCount immer verringern
}
