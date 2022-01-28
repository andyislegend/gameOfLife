package com.avenga.render.impl;

import com.avenga.model.CellState;
import com.avenga.render.GridContainer;

public class CellStateContainer implements GridContainer {

    private final CellState[][] matrix;

    public CellStateContainer(CellState[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public int getNumberOfRows() {
        return matrix.length;
    }

    @Override
    public int getNumberOfColumns() {
        return matrix[0].length;
    }

    @Override
    public boolean isCellAlive(int row, int col) {
        return matrix[row][col] == CellState.ALIVE;
    }

    public CellState[][] getGrid() {
        return matrix;
    }
}
