package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.Random;

public class PatternChess implements Pattern{

    private static final int BLACK = 0, WHITE = 1;
    private static final int DOWNLEFT = 0, UPLEFT = 1, DOWNRIGHT = 2, UPRIGHT = 3;

    private int color;

    private int start;
    private int size;
    private Vector2i lastIndex;

    public PatternChess(int size) {
        this.size = size;
        Random r = new Random();
        start = r.nextInt(4);
        color = r.nextInt(2);
    }

    @Override
    public Vector2i nextIndex() {
        if(start == DOWNLEFT || start == UPLEFT) {
            lastIndex.x -= 2;
            if (lastIndex.x < 1) {
                lastIndex.x += (size + (lastIndex.x == 0 ? -1 : 1));
                lastIndex.y += start == DOWNLEFT ? 1 : -1;
            }
            if (lastIndex.y > size || lastIndex.y < 1)
                return null;
        }
        if(start == DOWNRIGHT || start == UPRIGHT) {
            lastIndex.x += 2;
            if (lastIndex.x > size) {
                lastIndex.x -= (size + (lastIndex.x == size + 1 ? -1 : 1));
                lastIndex.y += start == DOWNRIGHT ? 1 : -1;
            }
            if (lastIndex.y > size || lastIndex.y < 1)
                return null;
        }
        return lastIndex;
    }

    @Override
    public Vector2i firstIndex() {
        switch (start){
            case DOWNLEFT: lastIndex = new Vector2i(size,1); break;
            case DOWNRIGHT: lastIndex = new Vector2i(1,1); break;
            case UPLEFT: lastIndex = new Vector2i(size,size); break;
            case UPRIGHT: lastIndex = new Vector2i(1, size); break;
        }
        if((lastIndex.x + lastIndex.y) % 2 != 1 && color == WHITE){
            if(start == UPRIGHT)
                lastIndex.x += 1;
            if(start == DOWNLEFT)
                lastIndex.x -= 1;
        }
        if((lastIndex.x + lastIndex.y) % 2 != 0 && color == BLACK){
            if(start == DOWNRIGHT)
                lastIndex.x += 1;
            if(start == UPLEFT)
                lastIndex.x -= 1;
        }
        return lastIndex;
    }
}
