package com.avenga.game.processor.impl;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.aggregator.CellVisitor;
import com.avenga.game.processor.CellProcessor;
import com.avenga.model.Cell;
import com.avenga.model.CellState;
import com.avenga.model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.avenga.util.ConcurrentUtil.tryToAwait;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

public class DecentralizedCellProcessor implements Runnable, CellProcessor {

    private final Cell cell;
    private final Map<Point, CellState> neighbours;
    private final List<CellProcessor> neighbourProcessors;
    private final CellVisitor visitor;

    private final CyclicBarrier barrier;
    private final int barrierTimeoutInSeconds;

    private final AtomicInteger neighbourCheckVersion;
    private final boolean enableCellSync;

    private volatile boolean start;

    public DecentralizedCellProcessor(Cell cell,
                                      List<Cell> neighbours,
                                      CellVisitor visitor,
                                      CyclicBarrier barrier,
                                      ApplicationProperties properties) {
        this.cell = cell;
        this.barrier = barrier;
        this.neighbours = neighbours.stream()
                .collect(toMap(Cell::getCoordinates, Cell::getState, (c1, c2) -> c1, ConcurrentHashMap::new));
        this.neighbourProcessors = new ArrayList<>(neighbours.size());
        this.visitor = visitor;
        this.barrierTimeoutInSeconds = properties.getInt("barrier.timeout-sec");
        this.enableCellSync = properties.getBoolean("cell.sync");

        this.neighbourCheckVersion = new AtomicInteger(0);
        this.start = true;
    }

    @Override
    public void addNeighbourProcessors(List<CellProcessor> neighbourProcessors) {
        this.neighbourProcessors.addAll(neighbourProcessors);
    }

    @Override
    public void run() {
        while (start) {
            int neighboursCount = countAliveCellNeighbours();
            if (cell.isAlive() && (neighboursCount < 2 || neighboursCount > 3)) {
                cell.kill();
                sendSelfCellStateToNeighbours();
            } else if (cell.isDead() && neighboursCount == 3) {
                cell.resurrect();
                sendSelfCellStateToNeighbours();
            }
            visitor.visit(cell);
            waitOthers();
        }
    }

    private int countAliveCellNeighbours() {
        int neighboursCount = (int) neighbours.values()
                .stream()
                .filter(state -> state == CellState.ALIVE)
                .count();
        neighbourCheckVersion.incrementAndGet();
        return neighboursCount;
    }

    private void sendSelfCellStateToNeighbours() {
        for (CellProcessor neighbourProcessor : neighbourProcessors) {
            neighbourProcessor.updateNeighbourState(cell, neighbourCheckVersion.get());
        }
    }

    @Override
    public void updateNeighbourState(Cell neighbour, int version) {
        if (enableCellSync) {
            for (; ; ) {
                if (version == this.neighbourCheckVersion.get()) {
                    break;
                }
                Thread.yield();
            }
        }
        neighbours.put(neighbour.getCoordinates(), neighbour.getState());
    }

    private void waitOthers() {
        tryToAwait(barrier, barrierTimeoutInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void finish() {
        start = false;
    }

    @Override
    public String toString() {
        return format("Cell processor [%d][%d]", cell.getCoordinates().x(), cell.getCoordinates().y());
    }
}
