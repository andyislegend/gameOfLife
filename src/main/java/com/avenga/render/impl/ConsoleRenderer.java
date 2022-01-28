package com.avenga.render.impl;

import com.avenga.render.GridContainer;
import com.avenga.render.GridRenderer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ConsoleRenderer implements GridRenderer {

    private static final String ALIVE_CELL_SYMBOL = "*";
    private static final String DEAD_CELL_SYMBOL = ".";

    private static final long DEFAULT_TIMEOUT_BETWEEN_PRINTS_IN_SEC = 1L;

    @Override
    public void render(GridContainer grid) {
        System.out.println();
        for (int i = 0; i < grid.getNumberOfRows(); i++) {
            for (int j = 0; j < grid.getNumberOfColumns(); j++) {
                System.out.printf("%s ", grid.isCellAlive(i, j) ? ALIVE_CELL_SYMBOL : DEAD_CELL_SYMBOL);
            }
            System.out.println();
        }
        waitBetweenPrints();
    }

    private static void waitBetweenPrints() {
        try {
            TimeUnit.SECONDS.sleep(DEFAULT_TIMEOUT_BETWEEN_PRINTS_IN_SEC);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted: ", e);
        }
    }
}
