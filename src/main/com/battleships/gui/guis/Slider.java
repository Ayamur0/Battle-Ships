package com.battleships.gui.guis;

import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.List;

public class Slider extends GuiClickCallback implements Runnable{

    private GuiTexture slider;
    private GuiTexture bar;

    private int minValue;
    private int maxValue;

    private float maxPosX;
    private float minPosX;

    private float mousePosX;

    private boolean running;

    /**
     * Creates a slider gui element, used to enter values that have to be within a specific range.
     * @param sliderTexture - Texture Id for the texture of the slider.
     * @param barTexture - Texture Id for the texture of the bar the slider is moving on.
     * @param minValue - lowest value the slider can have (when slider is completely to the left)
     * @param maxValue - highest value the slider can have (when slider is completely to the right)
     * @param defaultValue - value the slider start at, before it's moved by the user.
     * @param scale - How much space of the screen this slider should occupy (1 is full screen width).
     * @param position - Position of the center of this slider on the screen (Screen coordinates).
     * @param guiManager - GuiManager that should handle the click function of this slider.
     * @param guis - List of guis this slider should be added to. Needs to be passed to a renderer for this slider to show on screen.
     */
    public Slider(int sliderTexture, int barTexture, int minValue, int maxValue, int defaultValue, Vector2f scale, Vector2f position, GuiManager guiManager, List<GuiTexture> guis) {
        bar = new GuiTexture(barTexture, new Vector2f(position), scale);
        slider = new GuiTexture(sliderTexture, new Vector2f(position), new Vector2f(scale.x * 0.05f, scale.y * 3));
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.maxPosX = position.x + scale.x / 2;
        this.minPosX = position.x - scale.x / 2;
        setToValue(defaultValue);
        guis.add(slider);
        guis.add(bar);
        guiManager.createClickableGui(slider, () -> this);
    }

    /**
     * Tests if the click of the user was either on the slider or the bar.
     * @param gui - GuiElement on which the click should be (in this case the slider)
     * @param x - xPos of the click.
     * @param y - yPos of the click.
     * @return - {@code true} if the click was on the slider or bar, {@code false} else.
     */
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        mousePosX = (float)x;
        return super.isClickOnGui(gui, x, y) || super.isClickOnGui(bar, x, y);
    }

    /**
     * Starts the moving of the slider in another thread. Slider is moving
     * until mouse button is released.
     */
    @Override
    protected void clickAction() {
        if(running)
            return;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * Moves the slider to the mouse position but always stays on the bar.
     * Stops moving as soon as the mouse button is released.
     */
    @Override
    public void run(){
        running = true;
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        while(GLFW.glfwGetMouseButton(WindowManager.getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) != GLFW.GLFW_RELEASE) {
            GLFW.glfwGetCursorPos(WindowManager.getWindow(), x, y);
            x.rewind();
            y.rewind();

            float xpos = (float) x.get() / WindowManager.getWidth();

            x.clear();
            y.clear();
            slider.getPositions().x = xpos;
            if (slider.getPositions().x > maxPosX)
                slider.getPositions().x = maxPosX;
            if (slider.getPositions().x < minPosX)
                slider.getPositions().x = minPosX;
            System.out.println((int)getValue());
        }
        running = false;

    }

    /**
     *
     * @return - Current value of the slider.
     */
    public float getValue(){
        float percentage = (slider.getPositions().x - minPosX) / (maxPosX - minPosX);
        return percentage * (maxValue - minValue) + minValue;
    }

    /**
     * Sets the slider position to a specific value.
     * @param value - Value the slider should be set to.
     */
    public void setToValue(float value){
        float percentage = value / ((maxValue - minValue) + minValue);
        slider.getPositions().x = percentage * (maxPosX - minPosX) + minPosX;
    }
}
