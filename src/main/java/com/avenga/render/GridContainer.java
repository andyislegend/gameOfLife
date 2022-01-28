package com.avenga.render;

public interface GridContainer {

    int getNumberOfRows();

    int getNumberOfColumns();

    boolean isCellAlive(int row, int col);
}
