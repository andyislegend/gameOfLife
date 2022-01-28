package com.avenga.game.aggregator;

import com.avenga.model.Cell;

public interface CellVisitor {

    void visit(Cell cell);
}
