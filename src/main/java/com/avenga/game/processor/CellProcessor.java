package com.avenga.game.processor;

import com.avenga.model.Cell;

import java.util.List;

public interface CellProcessor {

    void addNeighbourProcessors(List<CellProcessor> neighbourProcessors);

    void updateNeighbourState(Cell neighbour, int version);

    void start();

    void finish();
}
