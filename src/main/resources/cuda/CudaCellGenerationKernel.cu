extern "C"
__global__ void runCellGeneration(int* iteration, int numberOfRows, int numberOfCols)
{
    int threadId = blockIdx.x * blockDim.x + threadIdx.x;

    int row = threadId / numberOfRows;
    int col = threadId % numberOfCols;
    int cellState = iteration[threadId];
    int aliveNeighbours = 0;

     for (int i = -1; i <= 1; i++)
     {
        for (int j = -1; j <= 1; j++)
        {
                if ((i + row < 0 || i + row > numberOfRows - 1) ||
                    (i == 0 && j == 0) ||
                    (j + col < 0 || j + col > numberOfCols - 1))
                    {
                        continue;
                    }
                    aliveNeighbours += iteration[j + col + (i + row) * numberOfRows];
        }
     }

    __syncthreads();
    
    if(cellState == 1 && (aliveNeighbours < 2 || aliveNeighbours > 3))
    {
        iteration[threadId] = 0;
    }
    else if(cellState == 0 && aliveNeighbours == 3)
    {
        iteration[threadId] = 1;
    }
}