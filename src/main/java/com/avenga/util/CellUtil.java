package com.avenga.util;

import com.avenga.model.Cell;
import com.avenga.model.CellState;
import com.avenga.model.Point;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellUtil {

    public static List<Cell> findNeighbours(Point cellPosition, CellState[][] grid) {
        int numberOfRows = grid.length;
        int numberOfCols = grid[0].length;
        List<Cell> neighbours = new ArrayList<>(3);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int cellRow = cellPosition.x();
                int cellColumn = cellPosition.y();
                if (isRowOutOfBounds(i, cellRow, numberOfRows) ||
                        isColumnOutOfBounds(j, cellColumn, numberOfCols) ||
                        isSelf(i, j)) {
                    continue;
                }

                CellState cellState = grid[i + cellRow][j + cellColumn];
                Cell neighbour = new Cell(i + cellRow, j + cellColumn, cellState);
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

    private static boolean isRowOutOfBounds(int row, int cellRow, int numberOfRows) {
        return row + cellRow < 0 || row + cellRow > (numberOfRows - 1);
    }

    private static boolean isColumnOutOfBounds(int col, int cellColumn, int numberOfCols) {
        return col + cellColumn < 0 || col + cellColumn > (numberOfCols - 1);
    }

    private static boolean isSelf(int i, int j) {
        return i == 0 && j == 0;
    }
}
