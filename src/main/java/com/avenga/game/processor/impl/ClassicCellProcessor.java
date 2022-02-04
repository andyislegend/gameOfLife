package com.avenga.game.processor.impl;

import com.avenga.model.Point;
import com.avenga.util.CellUtil;

import java.util.concurrent.CountDownLatch;

public class ClassicCellProcessor implements Runnable {

    private final Point position;
    private final int[][] currentGrid;
    private final int[][] nextGrid;
    private final int numberOfColumns;
    private final int numberOfRows;
    private final CountDownLatch waiter;

    public ClassicCellProcessor(Point position,
                                int[][] currentGrid,
                                int[][] nextGrid,
                                int numberOfRows,
                                int numberOfColumns,
                                CountDownLatch waiter) {
        this.position = position;
        this.currentGrid = currentGrid;
        this.nextGrid = nextGrid;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.waiter = waiter;
    }

    @Override
    public void run() {
        int aliveNeighbours = CellUtil.countAliveNeighbours(position, currentGrid, numberOfRows, numberOfColumns);
        checkRulesAndUpdateGrid(aliveNeighbours);
        waiter.countDown();
    }


    private void checkRulesAndUpdateGrid(int neighbours) {
        if (isCellAlive() && (neighbours < 2 || neighbours > 3)) {
            nextGrid[position.x()][position.y()] = 0;
        } else if (isCellDead() && neighbours == 3) {
            nextGrid[position.x()][position.y()] = 1;
        } else {
            nextGrid[position.x()][position.y()] = currentGrid[position.x()][position.y()];
        }
    }

    private boolean isCellAlive() {
        return currentGrid[position.x()][position.y()] == 1;
    }

    private boolean isCellDead() {
        return currentGrid[position.x()][position.y()] == 0;
    }
}
