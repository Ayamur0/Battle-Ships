package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.Random;

public class PatternX implements Pattern {

    private static final int DOWNLEFT = 0, UPLEFT = 1, DOWNRIGHT = 2, UPRIGHT = 3;

    private int lastX;
    private int lastY;
    private int size;
    private int direction;
    private int rowscomplete;

    public PatternX(int size) {
        this.size = size;
    }

    @Override
    public Vector2i nextIndex() {
        switch (direction){
            case DOWNRIGHT: lastX++; lastY++; break;
            case DOWNLEFT: lastX--; lastY++; break;
            case UPLEFT: lastX--; lastY--; break;
            case UPRIGHT: lastX++; lastY--; break;
        }
        if(lastX == size+1 || lastX == 0) {
            rowscomplete++;
            if(rowscomplete == 2)
                return null;
            direction = chooseNextDirection();
            switch (direction){
                case DOWNRIGHT: lastX = 1; lastY = 1; break;
                case DOWNLEFT: lastX = size; lastY = 1; break;
                case UPLEFT: lastX = size; lastY = size; break;
                case UPRIGHT: lastX = 1; lastY = size; break;
            }
        }
        return new Vector2i(lastX, lastY);
    }

    private int chooseNextDirection(){
        Random r = new Random();
        int next = r.nextInt(1);
        if(direction == DOWNRIGHT || direction == UPLEFT){
            return next == 0 ? DOWNLEFT : UPRIGHT;
        }
        else{
            return next == 0 ? DOWNRIGHT : UPLEFT;
        }
    }

    @Override
    public Vector2i firstIndex() {
        Random r = new Random();
        lastX = r.nextInt(4);
        switch (lastX){
            case 0: lastX = 1; lastY = 1; direction = DOWNRIGHT; return new Vector2i(lastX,lastY);
            case 1: lastX = size; lastY = 1; direction = DOWNLEFT; return new Vector2i(lastX, lastY);
            case 2: lastX = 1; lastY = size; direction = UPRIGHT; return new Vector2i(lastX, lastY);
            case 3: lastX = size; lastY = size; direction = UPLEFT; return new Vector2i(lastX, lastY);
            default: lastX = 1; lastY = 1; direction = DOWNRIGHT; return new Vector2i(1,1);
        }
    }
}
