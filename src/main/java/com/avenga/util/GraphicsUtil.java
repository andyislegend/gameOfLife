package com.avenga.util;

import com.avenga.model.CellState;
import com.avenga.render.GridContainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphicsUtil {

    public static void fillGridCells(Graphics graphics, CellState[][] iteration, int scale) {
        for (int i = 0, y = 0; i < iteration.length; i++, y += scale) {
            for (int j = 0, x = 0; j < iteration[0].length; j++, x += scale) {
                if (iteration[i][j] == CellState.ALIVE) {
                    graphics.setColor(Color.BLACK);
                } else {
                    graphics.setColor(Color.WHITE);
                }
                graphics.fillRect(x, y, x + scale, y + scale);
            }
        }
    }

    public static void fillGridCells(Graphics graphics, int[][] iteration, int scale) {
        for (int i = 0, y = 0; i < iteration.length; i++, y += scale) {
            for (int j = 0, x = 0; j < iteration[0].length; j++, x += scale) {
                if (iteration[i][j] == 1) {
                    graphics.setColor(Color.BLACK);
                } else {
                    graphics.setColor(Color.WHITE);
                }
                graphics.fillRect(x, y, x + scale, y + scale);
            }
        }
    }

    public static void fillGridCells(Graphics graphics, int[] iteration, int numberOfRows, int numberOfCols, int scale) {
        for (int i = 0, y = 0; i < numberOfRows; i++, y += scale) {
            for (int j = 0, x = 0; j < numberOfCols; j++, x += scale) {
                if (iteration[j + i * numberOfRows] == 1) {
                    graphics.setColor(Color.BLACK);
                } else {
                    graphics.setColor(Color.WHITE);
                }
                graphics.fillRect(x, y, x + scale, y + scale);
            }
        }
    }

    public static void fillGridCells(Graphics graphics, GridContainer container, int scale) {
        for (int i = 0, y = 0; i < container.getNumberOfRows(); i++, y += scale) {
            for (int j = 0, x = 0; j < container.getNumberOfColumns(); j++, x += scale) {
                if (container.isCellAlive(i, j)) {
                    graphics.setColor(Color.BLACK);
                } else {
                    graphics.setColor(Color.WHITE);
                }
                graphics.fillRect(x, y, x + scale, y + scale);
            }
        }
    }
}
