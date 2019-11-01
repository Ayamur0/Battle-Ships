package com.battleships.gui.guis;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;
import java.util.List;

public class GuiClickCallback {

    private List<GuiTexture> guis = new ArrayList<>();

    public void addClickableGui(List<GuiTexture> gui){
        guis.addAll(gui);
    }

    public void addClickableGui(GuiTexture gui){
        guis.add(gui);
    }

    public GLFWMouseButtonCallback guiClick = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            //Test on which gui mouse is and call function of that gui
        }
    };
}
