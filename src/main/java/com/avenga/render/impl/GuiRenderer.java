package com.avenga.render.impl;

import com.avenga.render.GridContainer;
import com.avenga.render.GridRenderer;
import com.avenga.util.GraphicsUtil;
import com.avenga.view.GridPanel;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class GuiRenderer implements GridRenderer {

    private static final long DEFAULT_TIMEOUT_BETWEEN_PRINTS_IN_MS = 500L;

    private final GridPanel gridPanel;

    public GuiRenderer(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
    }

    @Override
    public void render(GridContainer grid) {
        Graphics graphics = gridPanel.getGraphics();
        if (graphics == null) {
            return;
        }
        GraphicsUtil.fillGridCells(graphics, grid, gridPanel.getScale());
        gridPanel.drawGrid(graphics);
        gridPanel.paintComponents(graphics);
        waitBetweenPrints();
    }

    private static void waitBetweenPrints() {
        try {
            TimeUnit.MILLISECONDS.sleep(DEFAULT_TIMEOUT_BETWEEN_PRINTS_IN_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted: ", e);
        }
    }
}
