package com.battleships.gui.guis;

import com.battleships.gui.gameAssets.GameManager;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * This class is determines what was clicked when the user clicks anywhere on the screen.
 * The GuiManager tests if any {@link GuiTexture} with click function was clicked.
 *
 * @author Tim Staudenmaier
 */
public class GuiManager {

    /**
     * HashMap containing all {@link GuiTexture} with click function.
     * Value is the class that realizes the click function by extending {@link GuiClickCallback}.
     */
    private Map<GuiTexture, Object> clickableGuis = new HashMap<>();
    /**
     * List of all {@link GuiTexture}s with click function.
     */
    private List<GuiTexture> guis = new ArrayList<>();

    /**
     * Add an action to a {@link GuiTexture} that gets execute when the gui element gets clicked on.
     * @param gui The {@link GuiTexture} the action should be added to.
     * @param action A class that extends {@link GuiClickCallback}, thus has a method to check whether it was clicked and
     *               one that needs to be executed when it was clicked, performing the desired action of the gui element.
     *               Class needs to be contained within a Supplier.
     */
    public <T extends GuiClickCallback> void createClickableGui(GuiTexture gui, Supplier<T> action){
        clickableGuis.put(gui, action.get());
    }

    /**
     * Removes the click function of the given {@link GuiTexture}.
     * @param gui The {@link GuiTexture} the click action should be removed from.
     * @return {@code true} if the gui had a click action that got removed, {@code false} else.
     */
    public boolean removeClickableGui(GuiTexture gui){
        if(clickableGuis.remove(gui) != null)
            return true;
        return false;
    }

    /**
     * Removes the click function from all guis that had one.
     */
    public void clearClickableGuis(){
        clickableGuis.clear();
    }

    /**
     * Function that gets called if the user left clicks anywhere in the game.
     * Gets the cursor position and tests all guis with a click action if they were
     * clicked on and if one was clicked executes the click action of that gui.
     * @return {@code true} if any gui with click function was clicked, {@code false} else
     */
    public boolean testGuiClick (float x, float y) {
        for (GuiTexture texture : clickableGuis.keySet()) {
            GuiClickCallback current = (GuiClickCallback) clickableGuis.get(texture);
            if (current.isClickOnGui(texture, x, y)) {
                current.clickAction();
                return true;
            }
        }
        return false;
    }

    /**
     * Renders all guis that have a click function to the screen.
     * @param renderer Renderer to use for rendering.
     */
    public void renderClickableGuis(GuiRenderer renderer){
        guis.clear();
        guis.addAll(clickableGuis.keySet());
        renderer.render(guis);
    }
}
