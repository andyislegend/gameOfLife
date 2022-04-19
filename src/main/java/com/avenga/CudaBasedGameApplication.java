package com.avenga;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.Game;
import com.avenga.game.impl.CudaCentralizedGame;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.render.impl.Int1DGridContainer;
import com.avenga.util.GridGenerator;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

public class CudaBasedGameApplication {

    public static void main(String[] args) {
        ApplicationProperties properties = new ApplicationProperties();
        int[][] zeroIteration = GridGenerator.generateGridFromResource(properties.getProperty("grid.filename"));
        Int1DGridContainer gridContainer = new Int1DGridContainer(zeroIteration);
        GridPanel gridPanel = new GridPanel(gridContainer, properties.getInt("grid.cell-scale"));
        Game game = new CudaCentralizedGame(gridContainer, new GuiRenderer(gridPanel), properties);
        new MainFrame(game, gridPanel);
    }
}
