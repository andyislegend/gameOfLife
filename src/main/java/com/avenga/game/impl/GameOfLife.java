package com.avenga.game.impl;

import com.avenga.game.CellGeneration;
import com.avenga.game.Game;
import com.avenga.render.GridRenderer;
import com.avenga.render.impl.Int2DGridContainer;

public class GameOfLife extends Thread implements Game {

    private final Int2DGridContainer gridContainer;
    private final GridRenderer renderer;

    private volatile boolean play;

    public GameOfLife(Int2DGridContainer gridContainer, GridRenderer renderer) {
        super("Game-Runner");
        this.gridContainer = gridContainer;
        this.renderer = renderer;
        this.play = true;
    }

    @Override
    public void run() {
        renderer.render(gridContainer);
        while (play) {
            var nextGeneration = new CellGeneration(gridContainer).run();
            gridContainer.resetGrid(nextGeneration);
            renderer.render(gridContainer);
        }
    }

    @Override
    public void terminate() {
        this.play = false;
    }
}
