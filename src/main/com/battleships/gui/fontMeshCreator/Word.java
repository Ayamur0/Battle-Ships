package com.battleships.gui.fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * A Word consisting of multiple characters.
 *
 * @author Tim Staudenmaier
 */
public class Word {

    private List<Character> characters = new ArrayList<Character>();
    private double width = 0;
    private double fontSize;

    /**
     * @param fontSize - size of the text the word is in
     */

    protected Word(double fontSize){
        this.fontSize = fontSize;
    }

    /**
     * add character at the end of this word and increase width of the word
     * @param character - the character to be added
     */
    protected void addCharacter(Character character){
        characters.add(character);
        width += character.getxAdvance() * fontSize;
    }

    /**
     * @return - list of all characters in the word
     */
    protected List<Character> getCharacters(){
        return characters;
    }

    /**
     * @return - width of the word on the screen
     */
    protected double getWordWidth(){
        return width;
    }

}
