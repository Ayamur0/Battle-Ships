package com.battleships.gui.gameAssets.ingameGui;

import com.battleships.gui.guis.GuiClickCallback;
import com.battleships.gui.guis.GuiTexture;

public class ShipSelector extends GuiClickCallback {

    @Override
    protected boolean isClickOnGui(GuiTexture gui, double x, double y){
        return true;
    }

    @Override
    protected void clickAction(){

    }
}
