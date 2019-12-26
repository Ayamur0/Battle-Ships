package com.battleships.gui.fontRendering;

import com.battleships.gui.fontMeshCreator.FontType;
import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontMeshCreator.TextMeshCreator;
import com.battleships.gui.fontMeshCreator.TextMeshData;
import com.battleships.gui.renderingEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for all text related things, handles rendering of texts.
 * This TextMaster needs to be always initialized before using texts.
 *
 * @author Tim Staudenmaier
 */

public class TextMaster {

    /**
     * @param texts - Stores all texts corresponding to a FontType that are currently rendered
     */
    private static Loader loader;
    private static Map<FontType, List<GUIText>> texts = new HashMap<>();
    private static FontRenderer renderer;

    /**
     * Setup the TextMaster to be able to load and render text
     * @param loader2 - the loader to be used when loading the quads for the text to be rendered on
     */
    public static void init(Loader loader2){
        renderer = new FontRenderer();
        loader = loader2;
    }

    /**
     * Render all texts currently stored in texts
     */
    public static void render(){
        renderer.render(texts);
    }

    /**
     * Load a text, create it's meshes store these to the text and add the text to the texts HashMap so it gets rendered.
     * If the HashMap doesn't contain an entry with the used font, create a new one.
     *
     * @param text - the text to be loaded
     */
    public static void loadText(GUIText text){
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        addText(text);
    }

    /**
     * Adds a already loaded Text to the HashMap to be rendered.
     * @param text - text to add to the HashMap.
     */
    public static void addText(GUIText text){
        FontType font = text.getFont();
        List<GUIText> textBatch = texts.get(font);
        if(textBatch == null) {
            textBatch = new ArrayList<>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    /**
     * Remove a text from the texts hashMap so it doesn't get rendered anymore
     * @param text - the text to be removed
     */
    public static void removeText(GUIText text){
        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if(textBatch.isEmpty())
            texts.remove(text.getFont());
    }

    /**
     * Remove all texts from the screen.
     */
    public static void clear(){
        texts.clear();
    }

    /**
     * CleanUp text related stuff on program exit
     */
    public static void cleanUp(){
        renderer.cleanUp();
    }
}
