package com.avenga.game.impl;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.Game;
import com.avenga.game.aggregator.CellAggregator;
import com.avenga.game.processor.CellProcessor;
import com.avenga.game.processor.impl.DecentralizedCellProcessor;
import com.avenga.model.Cell;
import com.avenga.model.CellState;
import com.avenga.model.Point;
import com.avenga.render.GridRenderer;
import com.avenga.render.impl.CellStateContainer;
import com.avenga.util.CellUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import static java.lang.String.format;
import static java.lang.Thread.ofVirtual;
import static java.util.stream.Collectors.toList;

public class DecentralizedGame implements Game {

    private final Collection<CellProcessor> processors;
    private final List<Thread> processorVirtualThreads;

    public DecentralizedGame(ApplicationProperties properties,
                             CellStateContainer zeroGeneration,
                             GridRenderer renderer) {
        Map<Point, CellProcessor> cellProcessors = collectProcessors(properties, zeroGeneration, renderer);
        this.processorVirtualThreads = toVirtualThreads(cellProcessors);
        this.processors = cellProcessors.values();
    }

    private static Map<Point, CellProcessor> collectProcessors(ApplicationProperties properties,
                                                               CellStateContainer zeroGeneration,
                                                               GridRenderer renderer) {
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
                CellProcessor cellProcessor = new DecentralizedCellProcessor(cell, neighbours, cellAggregator, barrier, properties);

                cellsWithNeighbours.put(cellPosition, neighbours);
                cellsProcessors.put(cellPosition, cellProcessor);
            }
        }

        for (Map.Entry<Point, List<Cell>> entry : cellsWithNeighbours.entrySet()) {
            Point cellPosition = entry.getKey();
            CellProcessor processor = cellsProcessors.get(cellPosition);
            List<CellProcessor> neighbourProcessors = entry.getValue().stream().map(neighbour -> cellsProcessors.get(neighbour.getCoordinates())).toList();
            processor.addNeighbourProcessors(neighbourProcessors);
        }
        return cellsProcessors;
    }

    private static List<Thread> toVirtualThreads(Map<Point, CellProcessor> cellProcessors) {
        return cellProcessors.entrySet()
                .stream()
                .map(processor -> {
                    Point coordinates = processor.getKey();
                    return ofVirtual()
                            .name(format("Cell processor [%d][%d]", coordinates.x(), coordinates.y()))
                            .unstarted((Runnable) processor.getValue());
                })
                .collect(toList());
    }

    @Override
    public void start() {
        for (Thread virtualThread : processorVirtualThreads) {
            virtualThread.start();
        }
    }

    @Override
    public void terminate() {
        for (CellProcessor processor : processors) {
            processor.finish();
        }
    }
}
