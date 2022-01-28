package com.avenga.game.aggregator;

import com.avenga.model.Cell;
import com.avenga.model.CellState;
import com.avenga.model.Point;
import com.avenga.render.impl.CellStateContainer;
import com.avenga.render.GridRenderer;

public class CellAggregator implements CellVisitor, Runnable {

    private final CellStateContainer container;
    private final GridRenderer renderer;

    public CellAggregator(int numberOfRows, int numberOfCols, GridRenderer gridRenderer) {
        this.container = new CellStateContainer(new CellState[numberOfRows][numberOfCols]);
        this.renderer = gridRenderer;
    }

    @Override
    public void run() {
        renderer.render(container);
    }

    @Override
    public void visit(Cell cell) {
        Point coordinates = cell.getCoordinates();
        this.container.getGrid()[coordinates.x()][coordinates.y()] = cell.getState();
    }
}
