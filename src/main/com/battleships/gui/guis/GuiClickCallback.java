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
            for (GuiTexture i:guis) {
                int x = (int)(WindowManager.getWidth()*i.getPositions().x-(WindowManager.getWidth()*i.getScale().x)/2);
                int y = (int)(WindowManager.getHeight()*i.getPositions().y-(WindowManager.getHeight()*i.getScale().y/2));

                double []xpos = new double[1];
                double []ypos = new double[1];

                GLFW.glfwGetCursorPos(window,xpos,ypos);
                Rectangle dummy = new Rectangle(x,y,(int)(WindowManager.getWidth()*i.getScale().x),(int)(WindowManager.getHeight()*i.getScale().y));
                if (dummy.contains(xpos[0],ypos[0])){
                    System.out.println("Button: "+i);
                }
            }
            //Test on which gui mouse is and call function of that gui
        }
    };
}
