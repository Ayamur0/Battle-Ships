package com.battleships.logic.AI;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PatternLines implements Pattern{

    private static final int DOWNLEFT = 0, DOWNRIGHT = 1;
    private static final int RIGHT = 0, MIDDLERIGHT = 1, MIDDLELEFT = 2, LEFT = 3;

    private int distance;
    private int direction;
    private int startPoint;
    private List<Vector2i> lineStarts = new ArrayList<>();
    private int size;
    private Vector2i lastIndex;

    public PatternLines(int size) {
        this.size = size;
        Random r = new Random();
        distance = r.nextInt(3)+3;
        direction = r.nextInt(2);
        startPoint = r.nextInt(4);
        calculateLineStarts();
    }

    private void calculateLineStarts(){
        List<Vector2i> right = new ArrayList<>();
        List<Vector2i> left = new ArrayList<>();
        if(direction == DOWNLEFT) {
            int x = size, y = 1;
            while(y <= size) {
                right.add(new Vector2i(x, y));
                y += distance + 1;
            }
            y = 1;
            while(x > 0) {
                left.add(new Vector2i(x, y));
                x -= distance + 1;
            }
        }
        if(direction == DOWNRIGHT){
            int x = 1, y = 1;
            while(x <= size){
                right.add(new Vector2i(x,y));
                x += distance + 1;
            }
            x = 1;
            while (y <= size){
                left.add(new Vector2i(x,y));
                y += distance + 1;
            }
        }
        switch (startPoint){
            case RIGHT:
                Collections.reverse(right);
                lineStarts.addAll(right);
                lineStarts.addAll(left);
                break;
            case MIDDLERIGHT:
                lineStarts.addAll(right);
                lineStarts.addAll(left);
                break;
            case MIDDLELEFT:
                lineStarts.addAll(left);
                lineStarts.addAll(right);
            case LEFT:
                Collections.reverse(left);
                lineStarts.addAll(left);
                lineStarts.addAll(right);
        }
    }

    @Override
    public Vector2i nextIndex() {
        if(direction == DOWNLEFT) {
            lastIndex.x -= 1;
            lastIndex.y += 1;

            if (lastIndex.x < 1 || lastIndex.y > size) {
                if (lineStarts.size() == 0)
                    return null;
                lastIndex = lineStarts.get(0);
                lineStarts.remove(0);
            }
        }
        if(direction == DOWNRIGHT){
            lastIndex.x += 1;
            lastIndex.y += 1;

            if(lastIndex.x > size || lastIndex.y > size){
                if (lineStarts.size() == 0)
                    return null;
                lastIndex = lineStarts.get(0);
                lineStarts.remove(0);
            }
        }
        return lastIndex;
    }

    @Override
    public Vector2i firstIndex() {
        lastIndex = lineStarts.get(0);
        lineStarts.remove(0);
        return lastIndex;
    }
}
