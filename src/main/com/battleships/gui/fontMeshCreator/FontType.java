package com.battleships.gui.fontMeshCreator;

import java.io.File;

/**
 * Font capable of loading a mesh for a text string, so it can be rendered using this font.
 *
 * @author Tim Staudenmaier
 */

public class FontType {

    /**
     * Texture Atlas containing all character of this font.
     */
    private int textureAtlas;
    /**
     * TextMeshCreator holding all the data to be able to calculated the quads
     * a text needs to be rendered on if it uses this font.
     */
    private TextMeshCreator loader;

    /**
     * Creates a new font and loads up the data about each character from the
     * font file.
     *
     * @param textureAtlas - the ID of the font atlas texture.
     * @param fontFile - the font file containing information about each character in
     *            the texture atlas.
     */
    public FontType(int textureAtlas, String fontFile) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile);
    }

    /**
     * @return - The texture atlas of this font.
     */
    public int getTextureAtlas() {
        return textureAtlas;
    }

    /**
     * Takes in an unloaded text and calculate all of the vertices for the quads
     * on which this text will be rendered. The vertex positions and textureCoords
     * will be calculated based on the information from the font file.
     *
     * @param text - the unloaded text.
     * @return Information about the vertices of all the quads.
     */
    public TextMeshData loadText(GUIText text) {
        return loader.createTextMesh(text);
    }

}
