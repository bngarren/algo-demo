package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.AbstractAlgoWorker;
import com.bngarren.algodemo.util.ICell;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BFS extends AbstractAlgoWorker<List<ICell>, List<ICell>, BFSController> {

    public BFS(BFSController controller) {
        super(controller);
    }

    @Override
    protected List<ICell> runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException {
        List<ICell> result = new ArrayList<>();
        for (ICell cell : controller.getView().getCells().keySet()) {
            result.add(cell);
            publish(result);
            if (shouldStep) {
                semaphore.acquire();
            }
        }
        return result;
    }

    @Override
    protected void processChunk(List<ICell> chunk) {
        if (controller instanceof BFSController c) {
            for (ICell cell : chunk) {
                c.getView().getCell(cell.row(), cell.col()).setColors(Color.YELLOW, Color.BLACK, false);
            }
        }
    }
}
