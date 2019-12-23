package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.main.Inits;
import com.battleships.gui.renderingEngine.Loader;
import com.battleships.gui.window.WindowManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class ESCMenu extends MainMenuButton {
    public ESCMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);


        guiManager.clearClickableGuis();

        TextMaster.clear();

        this.createMenu();

        SetTextColor();

        CreateTextLabels();

    }
    private void createMenu(){

        super.CreateButtonTextures(4);


        super.guiTexts.add(new GUIText("Save", 3, font, new Vector2f(buttons.get(0).getPositions().x,buttons.get(0).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Resume", 3, font,new Vector2f(buttons.get(1).getPositions().x,buttons.get(1).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back to Main Menu", 3, font,new Vector2f(buttons.get(2).getPositions().x,buttons.get(2).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit to Desktop", 3, font,new Vector2f(buttons.get(3).getPositions().x,buttons.get(3).getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));

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
        if(super.isClickOnGui(super.buttons.get(3), x, y)) {
            super.buttonClicked = 3;
            return true;
        }
        return false;
    }

    @Override
    protected void clickAction() {
        if(buttonClicked == 0) {
            //TODO adding save thing
        }
        if(buttonClicked == 1){
            guiManager.clearClickableGuis();
            TextMaster.clear();
            //TODO check with tim
        }
        if (super.buttonClicked == 2){
            Inits.setStartMenu(new MainMenu(guiManager,loader));
        }
        if(buttonClicked == 3){
            GLFW.glfwSetWindowShouldClose(WindowManager.getWindow(),true);
        }
    }

}
