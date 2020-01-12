package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

/**
 * Contains needed buttons for the overall option menu
 *
 * @author Sascha Mößle
 */
public class OptionMenu extends InGameSettingsMenu {
    /**
     * Constant value for host button
     */
    private static final int POTATOMODE = 0;
    /**
     * Constant value for host button
     */
    private static final int BACK = 1;

    /**
     * Indicates if the potato mode is active or not
     */
    private static boolean potato = false;

    /**
     * Saves the volume option the user has set
     */
    private static int saveVolume = -1;

    /**
     * The texture for check box
     */
    private static int markTexture;

    /**
     * {@link Slider} used to set the volume
     */
    private Slider volume;

    /**
     * {@link GuiTexture} for the check box
     */
    GuiTexture marker;

    /**
     * Creates the option menu
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader Loader needed to load textures
     */
    public OptionMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader,4);
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    @Override
    protected void createMenu(){
        if (markTexture==0){
            markTexture = loader.loadTexture("buttonMark.png");
        }
        if (saveVolume == -1){
            saveVolume = 50;
        }
        volume = new Slider(loader.loadTexture("Slider.png"), loader.loadTexture("WoodenSlider.jpg"), 0, 100,
                saveVolume, new Vector2f(0.2f, 0.01f), super.standardButtonPos, guiManager, GameManager.getGuis());
        super.guiTexts.add(new GUIText("Volume: "+volume.getValueAsInt(),fontSize, font,new Vector2f(volume.getPositions().x,volume.getPositions().y-0.06f) , 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.buttons.add(new GuiTexture(buttonTexture,new Vector2f(volume.getPositions().x+0.06f,volume.getPositions().y+buttonGap),new Vector2f(0.1f,0.1f)));
        super.guiTexts.add(new GUIText("Potato mode",fontSize, font,new Vector2f(buttons.get(0).getPositions().x-0.14f,buttons.get(0).getPositions().y) , 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        super.buttons.add(new GuiTexture(buttonTexture,new Vector2f(0.5f ,0.83f), super.buttonSize));

        super.guiTexts.add(new GUIText("Back",fontSize, font, new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));

        marker = new GuiTexture(markTexture,buttons.get(0).getPositions(),buttons.get(0).getScale());

        super.createClickable();

        GameManager.getGuis().addAll(buttons);

        if (potato){
            GameManager.getGuis().add(marker);
        }

    }

    /**
     * Refreshes the {@link GUIText} that show the Value of the {@link Slider} in the current Menu
     */
    @Override
    public void RefreshSliderValue(){

        super.guiTexts.get(0).remove();
        super.guiTexts.get(0).setTextString("Volume: "+volume.getValueAsInt());

        saveVolume = volume.getValueAsInt();

        TextMaster.loadText(super.guiTexts.get(0));
    }
    /**
     * Indicates if the {@link Slider} is moving
     * @return {@code true} if the sliders is moving {@code false} else
     */
    @Override
    public boolean isRunning(){
        return (volume.isRunning());
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        if (super.buttonClicked == POTATOMODE){
            potato = !potato;
            if (potato)
                GameManager.getGuis().add(marker);
            else
                GameManager.getGuis().remove(marker);
            //TODO activate potato mode or deactivate potato mode
        }
        if (super.buttonClicked == BACK){
            super.clearMenu();
            MainMenuManager.setMenu(new MainMenu(guiManager,loader));
        }
    }
}
