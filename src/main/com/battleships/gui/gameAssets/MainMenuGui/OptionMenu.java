package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.guis.Slider;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;
import java.util.ArrayList;

/**
 * Contains needed buttons for the overall option menu
 *
 * @author Sascha Mößle
 */
public class OptionMenu extends InGameSettingsMenu {
    /**
     * Constant value for host button
     */
    private static final int SAVE = 0;
    /**
     * Constant value for back button
     */
    private static final int BACK = 1;
    /**
     * Constant value for plus button
     */
    private static final int PLUS = 2;
    /**
     * Constant value for minus button
     */
    private static final int MINUS = 3;
    /**
     * The texture used for the {@link GuiTexture} resolution settings button
     */
    private static int woodBox;

    /**
     * {@link Slider} used to set the volume
     */
    private Slider volume;
    /**
     * Used to save the resolution
     */
    private static int resNow;
    /**
     * Contains all available resolutions for the game
     */
    private static ArrayList<String> resolutions;

    /**
     * {@link GuiTexture} for the resolution settings button
     */
    GuiTexture textBoxWood;

    /**
     * Creates the option menu
     *
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param loader     Loader needed to load textures
     */
    public OptionMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader, 4);
    }

    /**
     * Creates {@link GUIText}as labels and adds the {@link GuiTexture} for the buttons.
     */
    @Override
    protected void createMenu() {
        initResolutions();

        if (woodBox == 0) {
            woodBox = loader.loadTexture("ResSelector.png");
        }

        volume = new Slider(loader.loadTexture("Slider.png"), loader.loadTexture("WoodenSlider.jpg"), 0, 100,
                (int)(GameManager.getSettings().getVolume()*50), new Vector2f(0.2f, 0.01f), super.standardButtonPos, guiManager, GameManager.getGuis());
        super.guiTexts.add(new GUIText("Volume: " + (int)volume.getValueAsFloat(), fontSize, font, new Vector2f(volume.getPositions().x, volume.getPositions().y - 0.06f), 0.2f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.guiTexts.add(new GUIText(resolutions.get(resNow), fontSize, font, new Vector2f(standardButtonPos.x,standardButtonPos.y+buttonGap), 0.3f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.buttons.add(new GuiTexture(buttonTexture,new Vector2f(standardButtonPos.x, standardButtonPos.y+2*buttonGap),buttonSize));
        super.buttons.add(new GuiTexture(buttonTexture,new Vector2f(standardButtonPos.x, standardButtonPos.y+buttonGap*3),buttonSize));

        super.buttons.add(new GuiTexture(woodBox,new Vector2f(standardButtonPos.x,standardButtonPos.y+buttonGap),new Vector2f(0.21f, 0.12f)));

        super.guiTexts.add(new GUIText("Save", fontSize, font,new Vector2f(buttons.get(0).getPositions()) , 0.2f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back", fontSize, font, new Vector2f(buttons.get(1).getPositions()), 0.2f, true, outlineColor, 0.0f, 0.1f, outlineColor, new Vector2f()));

        super.createClickable();

        GameManager.getGuis().addAll(buttons);
    }

    /**
     * Tests if the click was on either of the {@link GuiTexture}.
     * To test if the click was on the left or right side on the {@link GuiTexture} woodBox to set the resolution
     * @param gui The gui to test for if the click was on it.
     * @param x xPos of the click (left of screen = 0, right of screen = 1)
     * @param y yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return {@code true} if the click was on either of the{@link GuiTexture}, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        boolean dummy = super.isClickOnGui(gui,x,y);
        if (buttonClicked==2&&dummy) {
            float test = buttons.get(2).getScale().x /3.2f;
            if (x<buttons.get(2).getPositions().x-test){
                buttonClicked=3;
                return true;
            }
            else if (x>buttons.get(2).getPositions().x+test){
                buttonClicked=2;
                return true;
            }
            else
                return false;
        }
        return dummy;

    }

    /**
     * Refreshes the {@link GUIText} that show the Value of the {@link Slider} in the current Menu
     */
    @Override
    public void RefreshSliderValue() {

        super.guiTexts.get(0).remove();
        super.guiTexts.get(0).setTextString("Volume: " + volume.getValueAsInt());

        TextMaster.loadText(super.guiTexts.get(0));
    }

    /**
     * Indicates if the {@link Slider} is moving
     *
     * @return {@code true} if the sliders is moving {@code false} else
     */
    @Override
    public boolean isRunning() {
        return (volume.isRunning());
    }

    /**
     * Toggles state of clicked button.
     */
    @Override
    protected void clickAction() {
        int width, height;
        if (resolutions.get(resNow).equals("Adaptive")){
            width=-1;
            height=-1;
        }
        else{
            String dummy = resolutions.get(resNow).replace(" x ","/");
            String []resSolo = dummy.split("/");
            width= Integer.parseInt(resSolo[0]);
            height=Integer.parseInt(resSolo[1]);
        }
        if (super.buttonClicked == SAVE){
            GameManager.getSettings().setVolume(volume.getValueAsFloat()/50.0f);
            GameManager.getSettings().changeResolution(width,height);
            super.clearMenu();
            volume.remove();
            GameManager.getGuis().remove(textBoxWood);
            MainMenuManager.setMenu(new MainMenu(guiManager, loader));
        }
        if (super.buttonClicked == BACK) {
            super.clearMenu();
            volume.remove();
            GameManager.getGuis().remove(textBoxWood);
            MainMenuManager.setMenu(new MainMenu(guiManager, loader));
        }
        if (super.buttonClicked == PLUS) {
            setResolution(1);
        }
        if (super.buttonClicked == MINUS) {
            setResolution(-1);
        }

    }

    /**
     * Sets the Resolution for the game and updates the {@link GUIText}
     * @param amount how many steps to go in the {@link ArrayList} for the resolution
     */
    private void setResolution(int amount){
        if (resNow+amount<0)
            resNow=resolutions.size()-1;
        else if(resNow+amount>=resolutions.size())
            resNow=0;
        else
            resNow+=amount;
        super.guiTexts.get(1).remove();
        super.guiTexts.get(1).setTextString(resolutions.get(resNow));

        TextMaster.loadText(super.guiTexts.get(1));
    }

    /**
     * Initializes the {@link ArrayList} for the resolutions in the {@link GUIText}
     */
    private void initResolutions(){
        resolutions=new ArrayList<>();
        resolutions.add("800 x 600");
        resolutions.add("1280 x 720");
        resolutions.add("1920 x 1080");
        resolutions.add("2560 x 1440");
        resolutions.add("4096 x 2160");
        resolutions.add("Adaptive");

        String dummy = String.format("%d x %d",GameManager.getSettings().getResWidth(),GameManager.getSettings().getResHeight());

        for (int i = 0; i < resolutions.size(); i++) {
            if (dummy.equals(resolutions.get(i))){
                resNow=i;
                return;
            }
        }
        resNow=5;
    }
}
