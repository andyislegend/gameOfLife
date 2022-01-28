package com.avenga.render.impl;

import com.avenga.render.GridContainer;

public class Int2DGridContainer implements GridContainer {

    private int[][] grid2D;
    private final int numberOfRows;
    private final int numberOfColumns;

    public Int2DGridContainer(int[][] matrix) {
        grid2D = matrix;
        numberOfRows = matrix.length;
        numberOfColumns = matrix[0].length;
    }

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    @Override
    public boolean isCellAlive(int row, int col) {
        return grid2D[row][col] == 1;
    }

    public int getSize() {
        return numberOfColumns * numberOfRows;
    }

    public int[][] getGrid() {
        return grid2D;
    }

    public void setGrid(int[][] grid) {
        this.grid2D = grid;
    }
}
