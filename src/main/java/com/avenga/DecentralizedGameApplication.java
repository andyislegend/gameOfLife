package com.avenga;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.Game;
import com.avenga.game.impl.DecentralizedGame;
import com.avenga.model.CellState;
import com.avenga.render.impl.CellStateContainer;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

import static com.avenga.util.GridGenerator.generateCellStateGridFromResource;

public class DecentralizedGameApplication {

    public static void main(String[] args) {
        ApplicationProperties properties = new ApplicationProperties();
        CellState[][] zeroIteration = generateCellStateGridFromResource(properties.getProperty("grid.filename"));
        CellStateContainer wrappedZeroIteration = new CellStateContainer(zeroIteration);
        GridPanel gridPanel = new GridPanel(wrappedZeroIteration, properties.getInt("grid.cell-scale"));
        Game game = new DecentralizedGame(properties, wrappedZeroIteration, new GuiRenderer(gridPanel));
        new MainFrame(game, gridPanel);
    }
}
