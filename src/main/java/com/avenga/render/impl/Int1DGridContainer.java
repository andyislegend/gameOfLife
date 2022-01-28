package com.avenga.render.impl;

import com.avenga.render.GridContainer;
import com.avenga.util.ConversionUtil;

public class Int1DGridContainer implements GridContainer {

    private final int[] grid1D;
    private final int numberOfRows;
    private final int numberOfColumns;
    
    public Int1DGridContainer(int[][] matrix) {
        grid1D = ConversionUtil.convertToArray2(matrix);
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
        return grid1D[col + row * numberOfRows] == 1;
    }

    public int getSize() {
        return numberOfColumns * numberOfRows;
    }

    public int[] getGrid() {
        return grid1D;
    }
}
