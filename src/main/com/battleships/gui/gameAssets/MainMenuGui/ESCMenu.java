package com.battleships.gui.gameAssets.MainMenuGui;

import com.battleships.gui.fontMeshCreator.GUIText;
import com.battleships.gui.fontRendering.TextMaster;
import com.battleships.gui.guis.GuiManager;
import com.battleships.gui.guis.GuiTexture;
import com.battleships.gui.renderingEngine.Loader;
import org.joml.Vector2f;

public class ESCMenu extends MainMenuButton {
    private SaveButton saveButton;
    private ResumeButton resumeButton;
    private BackButton backButton;
    private ExitButton exitButton;

    private GuiTexture save;
    private GuiTexture resume;
    private GuiTexture back;
    private GuiTexture exit;

    public ESCMenu(GuiManager guiManager, Loader loader) {
        super(guiManager, loader);

        this.createButtons();

        this.createLabels();

    }

    protected void createButtons() {
        saveButton = new SaveButton(guiManager,loader);
        resumeButton = new ResumeButton(guiManager,loader);
        backButton = new BackButton(guiManager,loader,8);//TODO real back implement
        exitButton = new ExitButton();

        save = new GuiTexture(texture,standardButtonPos,buttonSize);
        resume = new GuiTexture(texture, new Vector2f(save.getPositions().x,save.getPositions().y+buttonGap), buttonSize);
        back = new GuiTexture(texture,new Vector2f(resume.getPositions().x,resume.getPositions().y+buttonGap),buttonSize);
        exit = new GuiTexture(texture,new Vector2f(resume.getPositions().x,resume.getPositions().y+buttonGap),buttonSize);
    }

    protected void createLabels() {
        super.guiTexts.add(new GUIText("Save",2.5f, font, new Vector2f(save.getPositions().x,save.getPositions().y), 0.12f, true, outlineColor,0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Resume", 2.5f, font, new Vector2f(resume.getPositions().x,resume.getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Back to Main Menu", 2.5f, font,new Vector2f(back.getPositions().x,back.getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));
        super.guiTexts.add(new GUIText("Exit to Desktop", 3, font,new Vector2f(exit.getPositions().x,exit.getPositions().y), 0.12f, true,outlineColor, 0.0f, 0.1f,outlineColor, new Vector2f()));

    }

    @Override
    protected void clickAction() {
        guiManager.clearClickableGuis();

        guiManager.createClickableGui(save,() -> saveButton);
        guiManager.createClickableGui(resume,() -> resumeButton);
        guiManager.createClickableGui(back,() -> backButton);
        guiManager.createClickableGui(exit,() -> exitButton);

        TextMaster.clear();
        super.CreateTextLabels();
    }
}
