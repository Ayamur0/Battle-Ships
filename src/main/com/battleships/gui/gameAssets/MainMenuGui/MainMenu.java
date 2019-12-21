package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MainMenu extends MainMenuButton {

    public MainMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);
        

        guiManager.clearClickableGuis();

        TextMaster.clear();

        this.createMenu();

        SetTextColor();

        CreateTextLabels();
        
    }

    private void createMenu(){

        super.buttons.add(new GuiTexture(texture, standardButtonPos, buttonSize));
        super.buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y+buttonGap),buttonSize));
        super.buttons.add(new GuiTexture(texture,new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y+buttonGap),buttonSize));

        super.guiTexts.add(new GUIText("Play", 3, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Options", 3, font,new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit", 3, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));

        super.createClickable();
    }
    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y) {
        if(super.isClickOnGui(super.buttons.get(0), x, y)) {
            super.buttonClicked = 0;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(1), x, y)) {
            super.buttonClicked = 1;
            return true;
        }
        if(super.isClickOnGui(super.buttons.get(2), x, y)) {
            super.buttonClicked = 2;
            return true;
        }
        return false;
    }

    @Override
    protected void clickAction() {
        if(buttonClicked == 0) {
            new PlayMenu(guiManager,loader);
        }
        if(buttonClicked == 1){
            new OptionMenu(guiManager,loader);
        }
        if (super.buttonClicked == 2){
            GLFW.glfwSetWindowShouldClose(WindowManager.getWindow(),true);
        }
    }

}
