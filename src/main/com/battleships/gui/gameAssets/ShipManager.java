package com.battleships.gui.gameAssets;

import com.battleships.gui.entities.Entity;
import com.battleships.gui.models.TexturedModel;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ShipManager {

    private List<Entity> ships = new ArrayList<>();

    public ShipManager(Loader loader){
        ships.add(loader.loadEntityfromOBJ("ship2", "ship2.tga", 10, 1));
        ships.add(loader.loadEntityfromOBJ("ship3", "ship3.jpg", 10, 1));
        ships.add(loader.loadEntityfromOBJ("ship4", "ship4.tga", 10, 1));
        ships.add(loader.loadEntityfromOBJ("ship5", "ship5.jpg", 10, 1));
    }

    public void placeShip(List<Entity> entities, int size, Vector3f position, Vector3f rotation, float scale){
        Entity toAdd = ships.get(size - 2);
        toAdd.setPosition(position);
        toAdd.setRotation(rotation);
        toAdd.setScale(scale);
        entities.add(toAdd);
    }
}
