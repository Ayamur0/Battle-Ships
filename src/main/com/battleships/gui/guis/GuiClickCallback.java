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

public abstract class GuiClickCallback {

    protected boolean isClickOnGui(GuiTexture gui, double x, double y){
        //check if cursor is on gui element
        if(gui.getPositions().x - 0.5f * gui.getScale().x <= x && gui.getPositions().x + 0.5f *
                gui.getScale().x >= x && gui.getPositions().y - 0.5f * gui.getScale().y <= y &&
                gui.getPositions().y + 0.5f * gui.getScale().y >= y){
            return true;
        }
        return false;
    }

    protected abstract void clickAction();
}
