package com.battleships.logic;

/**
 * Class that contains all information of the settings that need to be saved when the game is exited.
 *
 * @author Tim Staudenmaier
 */
public class SettingsSaveFile {

    /**
     * Saved value for Sound. {@code true} if sound is enabled, {@code false} else.
     */
    private boolean sound;

    /**
     * Saved value for animations. {@code true} if animations are enabled, {@code false} else.
     */
    private boolean animation;

    /**
     * Saved resolution of the game.
     */
    private int resWidth, resHeight;

    /**
     * Creates a file that saves the last set settings.
     * @param sound Value for sound that should be saved.
     * @param animation Value for animations that should be saved.
     * @param resWidth Resolution width that should be saved.
     * @param resHeight Resolution height that should be saved.
     */
    public SettingsSaveFile(boolean sound, boolean animation, int resWidth, int resHeight) {
        this.sound = sound;
        this.animation = animation;
        this.resWidth = resWidth;
        this.resHeight = resHeight;
    }

    /**
     * @return Last saved value of sound.
     */
    public boolean isSound() {
        return sound;
    }

    /**
     * @return Last saved value of animations.
     */
    public boolean isAnimation() {
        return animation;
    }

    /**
     * @return Last saved resolution width.
     */
    public int getResWidth() {
        return resWidth;
    }

    /**
     * @return Last saved resolution height.
     */
    public int getResHeight() {
        return resHeight;
    }


}
