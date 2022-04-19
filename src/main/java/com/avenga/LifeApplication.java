package com.avenga;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.Game;
import com.avenga.game.impl.GameOfLife;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.render.impl.Int2DGridContainer;
import com.avenga.util.GridGenerator;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

public class LifeApplication {

    public static void main(String[] args) {
        ApplicationProperties properties = new ApplicationProperties();
        int[][] zeroIteration = GridGenerator.generateGridFromResource(properties.getProperty("grid.filename"));
        Int2DGridContainer wrappedZeroIteration = new Int2DGridContainer(zeroIteration);
        GridPanel gridPanel = new GridPanel(wrappedZeroIteration, properties.getInt("grid.cell-scale"));
        Game game = new GameOfLife(wrappedZeroIteration, new GuiRenderer(gridPanel));
        new MainFrame(game, gridPanel);
    }
}
