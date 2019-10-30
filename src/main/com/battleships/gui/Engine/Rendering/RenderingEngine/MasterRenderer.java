package com.battleships.gui.Engine.Rendering.RenderingEngine;

import com.battleships.gui.Engine.Rendering.Entities.Camera;
import com.battleships.gui.Engine.Rendering.Entities.Entity;
import com.battleships.gui.Engine.Rendering.Entities.Light;
import com.battleships.gui.Engine.Rendering.Models.TexturedModel;
import com.battleships.gui.Engine.Rendering.Shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private StaticShader shader = new StaticShader();
    private Renderer renderer = new Renderer(shader);

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>(); //list all entities in one entry, that use the same texture

    //load things needed for rendering and then render all entities
    public void render(Light light, Camera camera){
        renderer.prepare();
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        //if a list with the texturedModel of the entity already exists in entities add entity to that list
        //else create new list in entities for the texturedModel of the entity
        if(batch != null){
            batch.add(entity);
        }else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp(){
        shader.cleanUp();
    }

}
