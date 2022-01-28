package com.avenga.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cell {

    private final Point coordinates;
    private CellState state;


    public Cell(int row, int col, CellState state) {
        this.coordinates = new Point(row, col);
        this.state = state;
    }

    public Cell(Point coordinates, CellState state) {
        this.coordinates = coordinates;
        this.state = state;
    }

    public boolean isAlive() {
        return state == CellState.ALIVE;
    }

    public boolean isDead() {
        return !isAlive();
    }

    public void resurrect() {
        state = CellState.ALIVE;
    }

    public void kill() {
        state = CellState.DEAD;
    }
}
