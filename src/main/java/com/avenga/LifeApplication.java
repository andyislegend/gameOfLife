package com.avenga;

import com.avenga.game.Game;
import com.avenga.game.impl.GameOfLife;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.render.impl.Int2DGridContainer;
import com.avenga.util.GridGenerator;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

public class LifeApplication {

    public static void main(String[] args) {
        int[][] zeroIteration = GridGenerator.generateGridFromFile("/" + args[0]);
        Int2DGridContainer wrappedZeroIteration = new Int2DGridContainer(zeroIteration);
        GridPanel gridPanel = new GridPanel(wrappedZeroIteration);
        Game game = new GameOfLife(wrappedZeroIteration, new GuiRenderer(gridPanel));
        new MainFrame(game, gridPanel);
    }
}
