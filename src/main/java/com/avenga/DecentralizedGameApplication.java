package com.avenga;

import com.avenga.game.DecentralizedGame;
import com.avenga.game.Game;
import com.avenga.model.CellState;
import com.avenga.render.impl.CellStateContainer;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.util.GridGenerator;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

public class DecentralizedGameApplication {

    public static void main(String[] args) {
        CellState[][] zeroIteration = GridGenerator.generateCellStateGridFromFile("/" + args[0]);
        CellStateContainer wrappedZeroIteration = new CellStateContainer(zeroIteration);
        GridPanel gridPanel = new GridPanel(wrappedZeroIteration);
        Game game = new DecentralizedGame(wrappedZeroIteration, new GuiRenderer(gridPanel));
        new MainFrame(game, gridPanel);
    }
}
