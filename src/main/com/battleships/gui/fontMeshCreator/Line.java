package com.battleships.gui.fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * One line of a {@link GUIText} consisting of multiple {@link Word}s.
 *
 * @author Tim Staudenmaier
 */
public class Line {

    /**
     * Maximum length this lane can reach.
     */
    private double maxLength;
    /**
     * Size of one space character.
     */
    private double spaceSize;

    /**
     * List of all the words in this line.
     */
    private List<Word> words = new ArrayList<Word>();
    /**
     * How long the line is with the current words.
     */
    private double currentLineLength = 0;

    /**
     * Create an empty line
     *
     * @param spaceWidth width of a space character
     * @param fontSize   size of the font
     * @param maxLength  the maximum length of a line before a line break
     */
    protected Line(double spaceWidth, double fontSize, double maxLength) {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }

    /**
     * Try to add a word to the line. If there is enough space for it, before the line
     * gets longer than the max length add the word and increase currentLineLength.
     *
     * @param word what word to try adding
     * @return {@code true} if the word has been successfully added to the line
     */
    protected boolean attemptToAddWord(Word word) {
        double additionalLength = word.getWordWidth();

        if (!words.isEmpty())
            additionalLength += spaceSize;

        if (currentLineLength + additionalLength <= maxLength) {
            words.add(word);
            currentLineLength += additionalLength;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return max length of the line before a line break
     */
    protected double getMaxLength() {
        return maxLength;
    }

    /**
     * @return current length of the line
     */
    protected double getLineLength() {
        return currentLineLength;
    }

    /**
     * @return list of all words in the line
     */
    protected List<Word> getWords() {
        return words;
    }

}
