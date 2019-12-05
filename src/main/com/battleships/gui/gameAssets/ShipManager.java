package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ShipManager {

    private Entity[] ships;

    public ShipManager(Loader loader){
        ships = new Entity[4];
        ships[0] = (loader.loadEntityfromOBJ("ship2", "ship2.tga", 10, 1));
        ships[1] = (loader.loadEntityfromOBJ("ship3", "ship3.jpg", 10, 1));
        ships[2] = (loader.loadEntityfromOBJ("ship4", "ship4.tga", 10, 1));
        ships[3] = (loader.loadEntityfromOBJ("ship5", "ship5.jpg", 10, 1));
    }

    public int placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        Entity toAdd = ships[size - 2];
        toAdd.setPosition(position);
        toAdd.setRotation(rotation);
        toAdd.setScale(scale);
        entities.add(toAdd);
        return entities.size() - 1;
    }

    public void stickShipToCursor(int shipSize){

    }
}
