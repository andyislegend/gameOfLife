package com.avenga.game;

import com.avenga.game.processor.impl.ClassicCellProcessor;
import com.avenga.model.Point;
import com.avenga.render.impl.Int2DGridContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CellGeneration {

    private static final long WAITING_TIME_FOR_CELL_PROCESSING_IN_SEC = 10L;

    private final int[][] grid;
    private final int numberOfRows;
    private final int numberOfColumns;
    private final CountDownLatch waiter;

    public CellGeneration(Int2DGridContainer gridContainer) {
        this.grid = gridContainer.getGrid();
        this.numberOfRows = gridContainer.getNumberOfRows();
        this.numberOfColumns = gridContainer.getNumberOfColumns();
        this.waiter = new CountDownLatch(this.numberOfColumns * this.numberOfColumns);
    }

    public int[][] run() {
        int[][] newGeneration = new int[numberOfRows][numberOfColumns];
        processCellsInParallel(newGeneration);
        return newGeneration;
    }

    private void processCellsInParallel(int[][] nextGrid) {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                var coordinates = new Point(row, col);
                var cellProcessor = new Thread(
                        new ClassicCellProcessor(coordinates, grid, nextGrid, numberOfRows, numberOfColumns, waiter),
                        String.format("Cell processor [%d][%d]", row, col)
                );
                cellProcessor.start();
            }
        }
        waitForOthers();
    }

    private void waitForOthers() {
        try {
            waiter.await(WAITING_TIME_FOR_CELL_PROCESSING_IN_SEC, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.debug("Interrupted", e);
        }
    }
}
