package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.audio.AudioMaster;
import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link GuiTexture}that holds symbols indicating if animation and sound are currently enabled or disabled.
 * Has a click function to change the state of these symbols.
 *
 * @author Tim Staudenmaier
 */
public class DisableSymbols extends GuiClickCallback {

    /**
     * Constant value for animation symbol.
     */
    public static final int ANIMATION = 0;
    /**
     * Constant value for sound symbol.
     */
    public static final int SOUND = 1;

    /**
     * TextureAtlas containing the 4 textures for the two symbols (two for each, enabled and disabled).
     */
    private static final String textureAtlas = "DisableSymbolsWood.png";
    /**
     * List containing only the two symbols.
     */
    private List<GuiTexture> currentSymbols = new ArrayList<>();
    /**
     * Used to determine which button was the last one that was clicked.
     */
    private int buttonClicked;

    /**
     * List of guis these two symbols should get added to, this list needs to be passed to
     * a {@link com.battleships.gui.guis.GuiRenderer} for these symbols to appear on screen.
     */
    private List<GuiTexture> guis;
    /**
     * {@link GuiManager} that handles the clickFunctions of the symbols.
     */
    private GuiManager guiManager;

    /**
     * Creates the gui-elements for the symbols that show whether sound or animations are enabled or disabled.
     * @param loader Loader needed to load textures
     * @param guiManager GuiManager that should handle the click function of these guis.
     * @param guis List of guis that these should be added to. This list needs to be rendered so these guis show on screen.
     */
    public DisableSymbols (Loader loader, GuiManager guiManager, List<GuiTexture> guis){
        this.guiManager = guiManager;
        this.guis = guis;
        int texture = loader.loadTexture(textureAtlas);
        GuiTexture animation = new GuiTexture(texture, new Vector2f(0.89f, 0.9f));
        animation.getScale().x /= 2;
        animation.getScale().y /= 2;
        animation.setRows(2);
        guiManager.createClickableGui(animation, () -> this);
        GuiTexture sound = new GuiTexture(texture, new Vector2f(animation.getPositions().x + animation.getScale().x + 0.02f, 0.9f));
        sound.getScale().x /= 2;
        sound.getScale().y /= 2;
        sound.setRows(2);
        sound.setOffsetY(1);
        guiManager.createClickableGui(sound, () -> this);
        currentSymbols.add(animation);
        currentSymbols.add(sound);
        guis.addAll(currentSymbols);
    }

    /**
     * Tests if the click was on either of the symbols.
     * @param gui The gui to test for if the click was on it.
     * @param x xPos of the click (left of screen = 0, right of screen = 1).
     * @param y yPos of the click (top of screen = 0, bottom of screen = 1).
     * @return {@code true} if the click was on either symbol, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(currentSymbols.get(0), x, y)) {
            buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(currentSymbols.get(1), x, y)) {
            buttonClicked = 1;
            return true;
        }
        return false;
    }

    /**
     * Toggles state of clicked symbol.
     */
    @Override
    protected void clickAction() {
        if(buttonClicked == ANIMATION) {
            GameManager.toggleAnimations();
            toggleSymbol(ANIMATION);
        }
        if(buttonClicked == SOUND) {
            if(currentSymbols.get(SOUND).getOffsetX() == 0)
                AudioMaster.changeVolume(0);
            else
                AudioMaster.changeVolume(1);
            toggleSymbol(SOUND);
        }
    }

    /**
     * Swaps the texture of a symbol between on and off.
     * @param symbol number of the symbol that should be toggled.
     */
    public void toggleSymbol(int symbol){
        currentSymbols.get(symbol).setOffsetX((currentSymbols.get(symbol).getOffsetX() * currentSymbols.get(symbol).getRows() + 1) % 2);
    }

    /**
     * Removes the gui elemnts of the disableSymbols from the screen.
     */
    public void remove(){
        for(GuiTexture g : currentSymbols){
            guiManager.removeClickableGui(g);
            guis.remove(g);
        }
    }
}
