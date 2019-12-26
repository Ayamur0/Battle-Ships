package com.battleships.gui.fontMeshCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class can create the quads a text needs to be rendered on, depending on which characters are in the text.
 * Each {@link FontType} has one TextMeshCreator to create the meshes for texts using that font.
 *
 * @author Tim Staudenmaier
 */
public class TextMeshCreator {

    /**
     * Height of a line in screen space.
     */
    protected static final double LINE_HEIGHT = 0.03f;
    /**
     * ASCII-Code of the space character.
     */
    protected static final int SPACE_ASCII = 32;

    private MetaFile metaData;

    /**
     * @param metaFile - {@link MetaFile} containing all the information about the font and it's characters.
     */

    protected TextMeshCreator(String metaFile) {
        metaData = new MetaFile(metaFile);
    }

    /**
     * Converts the text into {@link Line}, {@link Word} and {@link Character} components.
     * Then creates quads for each character to be rendered on.
     *
     * @param text - the text the mesh should be created for
     * @return A {@link TextMeshData} containing two float arrays one with the vertices and one with textureCoords for the quads
     */

    protected TextMeshData createTextMesh(GUIText text) {
        List<Line> lines = createStructure(text);
        TextMeshData data = createQuadVertices(text, lines);
        return data;
    }

    /**
     * Gets the characters of the text to be displayed from {@link GUIText}
     * converts character array into lines and words
     * 
     * @param text - the text to create a mesh structure for
     * @return A list of all lines of the text, each line containing the words in a line
     */
    private List<Line> createStructure(GUIText text) {
        char[] chars = text.getTextString().toCharArray();
        List<Line> lines = new ArrayList<>();
        Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        for (char c : chars) {
            int ascii = (int) c;
            //if space character is read, word is over
            if (ascii == SPACE_ASCII) {
                //try to add word to line
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.attemptToAddWord(currentWord);
                }
                //start new word
                currentWord = new Word(text.getFontSize());
                continue;
            }
            //get character from font by ascii value
            Character character = metaData.getCharacter(ascii);
            //add character to current word
            currentWord.addCharacter(character);
        }
        //add last word (no space after last word, so needs to be added extra)
        tryToAddWord(lines, currentLine, currentWord, text);
        //add last line to text
        lines.add(currentLine);
        return lines;
    }

    /**
     * Tries to add a word to the currentLine.
     * If word can be added it's added to the line.
     * If the line would exceed the maxLineLength after the word has been added,
     * a new line is created and the word is added into the new line.
     * 
     * @param lines - list to save the lines into   
     * @param currentLine - line that the words are currently written into
     * @param currentWord - word that needs to be added to the line
     * @param text - GUIText the word is from (containing font size etc.)
     */

    private void tryToAddWord(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.attemptToAddWord(currentWord);
        }
        
    }


    /**
     * Create quads for each character of the text to be rendered on.
     * Iterates over lines, words in lines and characters in words to create a quad
     * for each character.
     *
     * @param text - text the quads should be created for
     * @param lines - list of lines the text contains
     * @return A {@link TextMeshData} containing two float arrays one with the vertices and one with textureCoords for the quads
     */
    private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
        //set number of lines of the text which are now known
        text.setNumberOfLines(lines.size());
        double cursorX = 0f;
        double cursorY = 0f;
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        for (Line line : lines) {
            if (text.isCentered()) {
                //set cursor to beginning of the line
                cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
            for (Word word : line.getWords()) {
                for (Character letter : word.getCharacters()) {
                    addVerticesForCharacter(cursorX, cursorY, letter, text.getFontSize(), vertices);
                    addCoordinates(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
                            letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    cursorX += letter.getxAdvance() * text.getFontSize();
                }
                cursorX += metaData.getSpaceWidth() * text.getFontSize();
            }
            cursorX = 0;
            cursorY += LINE_HEIGHT * text.getFontSize();
        }
        return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
    }

    /**
     * Creates a quad made of 4 vertices the character can be rendered on
     *
     * @param cursorX - current x position of cursor
     * @param cursorY - current y position of cursor
     * @param character - character the quad should be made for
     * @param fontSize - size of the font
     * @param vertices - list the vertices should be stored to
     */
    private void addVerticesForCharacter(double cursorX, double cursorY, Character character, double fontSize, List<Float> vertices) {
        double x = cursorX + (character.getxOffset() * fontSize);
        double y = cursorY + (character.getyOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addCoordinates(vertices, properX, properY, properMaxX, properMaxY);
    }

    /**
     * Adds vertices or textureCoords to the corresponding list in the right format:
     * each quad is made of 2 triangles, for each triangle all 3 corners must be added to the list
     *
     * @param coordinates - list ti store vertices or textureCoords to
     * @param x - lower x value of the quad
     * @param y - lower y value of the quad
     * @param maxX - higher x value of the quad
     * @param maxY - higher y value of the quad
     */
    private static void addCoordinates(List<Float> coordinates, double x, double y, double maxX, double maxY) {
        double[] toAdd = {x, y, x, maxY, maxX, maxY, maxX, maxY, maxX, y, x, y};
        for(double i : toAdd)
            coordinates.add((float)i);
    }

    /**
     * convert a list to a array
     * @param listOfFloats - list to be converted
     * @return array containing all values of the list in the same order as they were in the list
     */
    private static float[] listToArray(List<Float> listOfFloats) {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = listOfFloats.get(i);
        }
        return array;
    }

    /**
     * @return - the max height of a line in screen coordinates (0.0 - 1.0)
     */
    public static double getLineHeight() {
        return LINE_HEIGHT;
    }
}