package com.avenga.game;

import com.avenga.game.aggregator.CellAggregator;
import com.avenga.game.processor.CellProcessor;
import com.avenga.game.processor.impl.DecentralizedCellProcessor;
import com.avenga.model.Cell;
import com.avenga.model.CellState;
import com.avenga.model.Point;
import com.avenga.render.impl.CellStateContainer;
import com.avenga.render.GridRenderer;
import com.avenga.util.CellUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class DecentralizedGame implements Game {

    private final Map<Point, CellProcessor> processors;

    public DecentralizedGame(CellStateContainer zeroGeneration, GridRenderer renderer) {
        processors = init(zeroGeneration, renderer);
    }

    private Map<Point, CellProcessor> init(CellStateContainer zeroGeneration, GridRenderer renderer) {
        int numberOfRows = zeroGeneration.getNumberOfRows();
        int numberOfCols = zeroGeneration.getNumberOfColumns();

        CellAggregator cellAggregator = new CellAggregator(numberOfRows, numberOfCols, renderer);
        CyclicBarrier barrier = new CyclicBarrier(numberOfRows * numberOfCols, cellAggregator);
        Map<Point, CellProcessor> cellsProcessors = new HashMap<>();
        Map<Point, List<Cell>> cellsWithNeighbours = new HashMap<>();
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfCols; j++) {
                Point cellPosition = new Point(i, j);
                CellState cellState = zeroGeneration.getGrid()[i][j];
                Cell cell = new Cell(cellPosition, cellState);
                List<Cell> neighbours = CellUtil.findNeighbours(cellPosition, zeroGeneration.getGrid());
                DecentralizedCellProcessor cellProcessor = new DecentralizedCellProcessor(cell, neighbours, cellAggregator, barrier);
                cellProcessor.setName(String.format("Cell processor [%d][%d]", i, j));

                cellsWithNeighbours.put(cellPosition, neighbours);
                cellsProcessors.put(cellPosition, cellProcessor);
            }
        }

        for (Map.Entry<Point, List<Cell>> entry : cellsWithNeighbours.entrySet()) {
            Point cellPosition = entry.getKey();
            CellProcessor processor = cellsProcessors.get(cellPosition);
            List<CellProcessor> neighbourProcessors = entry.getValue()
                    .stream()
                    .map(neighbour -> cellsProcessors.get(neighbour.getCoordinates()))
                    .toList();
            processor.addNeighbourProcessors(neighbourProcessors);
        }
        return cellsProcessors;
    }

    @Override
    public void start() {
        for (CellProcessor processor : processors.values()) {
            processor.start();
        }
    }

    @Override
    public void stop() {
        for (CellProcessor processor : processors.values()) {
            processor.finish();
        }
    }
}
