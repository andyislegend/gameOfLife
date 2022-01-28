package com.avenga.view;

import com.avenga.render.GridContainer;
import com.avenga.util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {

    private static final int DEFAULT_GRID_SCALE = 20;

    private final int scale;
    private final GridContainer zeroIteration;

    public GridPanel(GridContainer zeroIteration) {
        this(zeroIteration, DEFAULT_GRID_SCALE);
    }

    public GridPanel(GridContainer zeroIteration, int scale) {
        this.zeroIteration = zeroIteration;
        this.scale = scale;
        setSize(zeroIteration.getNumberOfColumns() * scale, zeroIteration.getNumberOfRows() * scale);
        setPreferredSize(this.getSize());
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GraphicsUtil.fillGridCells(g, zeroIteration, scale);
        drawGrid(g);
    }

    public void drawGrid(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        for (int x = 0; x < getWidth(); x += scale) {
            graphics.drawLine(x, 0, x, getHeight() + scale);
        }

        for (int y = 0; y < getHeight(); y += scale) {
            graphics.drawLine(0, y, getWidth() + scale, y);
        }
    }

    public int getScale() {
        return scale;
    }
}
