package com.avenga;

import com.avenga.game.Game;
import com.avenga.game.impl.CudaCentralizedGame;
import com.avenga.render.impl.GuiRenderer;
import com.avenga.render.impl.Int1DGridContainer;
import com.avenga.util.GridGenerator;
import com.avenga.view.GridPanel;
import com.avenga.view.MainFrame;

public class CudaBasedGameApplication {

    public static void main(String[] args) {
        int[][] zeroIteration = GridGenerator.generateGridFromFile("/pulsar");
        // int[][] zeroIteration = GridGenerator.generateGrid(30);
        Int1DGridContainer gridContainer = new Int1DGridContainer(zeroIteration);

        GridPanel gridPanel = new GridPanel(gridContainer);
        Game game = new CudaCentralizedGame(gridContainer, new GuiRenderer(gridPanel));
        new MainFrame(game, gridPanel);
    }
}
