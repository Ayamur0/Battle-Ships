package com.battleships.gui.guis;

import com.battleships.gui.window.WindowManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GuiManager {

    private Map<GuiTexture, Object> clickableGuis = new HashMap<>();
    private List<GuiTexture> guis = new ArrayList<>();
    private DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
    private DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

    /**
     * Add an action to a {@link GuiTexture} that gets execute when the gui element gets clicked on.
     * @param gui - The {@link GuiTexture} the action should be added to.
     * @param action - A class that extends {@link GuiClickCallback}, thus has a method to check whether it was clicked and
     *               one that needs to be executed when it was clicked, performing the desired action of the gui element.
     *               Class needs to be contained within a Supplier.
     */
    public <T extends GuiClickCallback> void createClickableGui(GuiTexture gui, Supplier<T> action){
        clickableGuis.put(gui, action.get());
    }

    /**
     * Removes the click function of the given {@link GuiTexture}.
     * @param gui - The {@link GuiTexture} the click action should be removed from.
     * @return - {@code true} if the gui had a click action that got removed, {@code false} else.
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
     */
    public GLFWMouseButtonCallback testGuiClick = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {

                GLFW.glfwGetCursorPos(window, x, y);
                x.rewind();
                y.rewind();

                float xpos = (float)x.get() / WindowManager.getWidth();
                float ypos = (float)y.get() / WindowManager.getHeight();

                x = BufferUtils.createDoubleBuffer(1);
                y = BufferUtils.createDoubleBuffer(1);

            System.out.println(xpos + " " + ypos);

                for(GuiTexture texture : clickableGuis.keySet()){
                    GuiClickCallback current = (GuiClickCallback)clickableGuis.get(texture);
                    if(current.isClickOnGui(texture, xpos, ypos)) {
                        current.clickAction();
                        break;
                    }
                }
        }
    };

    /**
     * Renders all guis that have a click function to the screen.
     * @param renderer - Renderer to use for rendering.
     */
    public void renderClickableGuis(GuiRenderer renderer){
        guis.clear();
        guis.addAll(clickableGuis.keySet());
        renderer.render(guis);
    }
}
