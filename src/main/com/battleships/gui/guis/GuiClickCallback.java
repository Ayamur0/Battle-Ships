package com.battleships.gui.guis;

import com.battleships.gui.window.WindowManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Every gui class that needs to be clickable has to extends this class.
 *
 * @author Tim Staudenmaier
 */
public abstract class GuiClickCallback {

    /**
     * Tests if a click was on a gui element.
     * @param gui - The gui to test for if the click was on it.
     * @param x - xPos of the click (left of screen = 0, right of screen = 1)
     * @param y - yPos of the click (top of screen = 0, bottom of screen = 1)
     * @return - {@code true} if the click was on the gui, {@code false} else.
     */
    protected boolean isClickOnGui(GuiTexture gui, double x, double y){
        //check if cursor is on gui element
        if(gui.getPositions().x - 0.5f * gui.getScale().x <= x && gui.getPositions().x + 0.5f *
                gui.getScale().x >= x && gui.getPositions().y - 0.5f * gui.getScale().y <= y &&
                gui.getPositions().y + 0.5f * gui.getScale().y >= y){
            return true;
        }
        return false;
    }

    /**
     * Function that gets called if the gui element was clicked.
     */
    protected abstract void clickAction();
}
