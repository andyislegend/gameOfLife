package com.avenga.game.impl;

import com.avenga.config.ApplicationProperties;
import com.avenga.game.Game;
import com.avenga.game.processor.impl.CudaGridProcessor;
import com.avenga.render.GridRenderer;
import com.avenga.render.impl.Int1DGridContainer;

public class CudaCentralizedGame implements Game {

    private final CudaGridProcessor gridProcessor;

    public CudaCentralizedGame(Int1DGridContainer zeroIteration, GridRenderer renderer, ApplicationProperties properties) {
        String kernelFilename = properties.getProperty("cuda.kernel-filename");
        int maxThreadsPerBlock = properties.getInt("cuda.max-threads-per-block");
        this.gridProcessor = new CudaGridProcessor(zeroIteration, renderer, kernelFilename, maxThreadsPerBlock);
    }

    @Override
    public void start() {
        gridProcessor.start();
    }

    @Override
    public void terminate() {
        gridProcessor.finish();
    }
}
