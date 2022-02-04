package com.avenga.game.processor.impl;

import com.avenga.render.GridRenderer;
import com.avenga.render.impl.Int1DGridContainer;
import com.avenga.util.CudaUtil;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.*;

import static jcuda.driver.JCudaDriver.*;

public class CudaGridProcessor extends Thread {

    private static final String KERNEL_FILE_PATH = "/cuda/CudaCellGenerationKernel.cu";
    private static final int MAX_THREADS_PER_BLOCK = 1024;

    private final Int1DGridContainer gridContainer;
    private final GridRenderer renderer;
    private final int cudaGridSize;
    private final CUdeviceptr gridPointer;

    private volatile boolean start;

    public CudaGridProcessor(Int1DGridContainer gridContainer, GridRenderer renderer) {
        this.gridContainer = gridContainer;
        this.renderer = renderer;
        this.cudaGridSize = (int) Math.ceil((double) gridContainer.getSize() / MAX_THREADS_PER_BLOCK);
        this.gridPointer = new CUdeviceptr();

        this.start = true;
    }

    @Override
    public void run() {
        JCudaDriver.setExceptionsEnabled(true);
        initDevice();
        CUfunction kernel = prepareKernel();
        Pointer kernelParameters = Pointer.to(
                Pointer.to(gridPointer),
                Pointer.to(new int[]{gridContainer.getNumberOfRows()}),
                Pointer.to(new int[]{gridContainer.getNumberOfColumns()})
        );

        while (start) {
            cuLaunchKernel(kernel,
                    cudaGridSize, 1, 1,        // Grid dimension
                    MAX_THREADS_PER_BLOCK, 1, 1,   // Block dimension
                    0, null,             // Shared memory size and stream
                    kernelParameters, null               // Kernel- and extra parameters
            );
            cuCtxSynchronize();
            cuMemcpyDtoH(Pointer.to(gridContainer.getGrid()), gridPointer, (long) gridContainer.getSize() * Sizeof.INT);

            renderer.render(gridContainer);
        }
    }

    private static void initDevice() {
        cuInit(0);
        CUdevice device = new CUdevice();
        cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        cuCtxCreate(context, 0, device);
    }

    private CUfunction prepareKernel() {
        // Load the cubin file
        String cubinFileName = CudaUtil.prepareDefaultCubinFile(KERNEL_FILE_PATH);
        CUmodule module = new CUmodule();
        cuModuleLoad(module, cubinFileName);

        // Obtain a function pointer to the "runCellGeneration" function.
        CUfunction runCellGenerationKernel = new CUfunction();
        cuModuleGetFunction(runCellGenerationKernel, module, "runCellGeneration");

        cuMemAlloc(gridPointer, (long) gridContainer.getSize() * Sizeof.INT);
        cuMemcpyHtoD(gridPointer, Pointer.to(gridContainer.getGrid()), (long) gridContainer.getSize() * Sizeof.INT);
        return runCellGenerationKernel;
    }

    public void finish() {
        this.start = false;
        cuMemFree(gridPointer);
    }
}
