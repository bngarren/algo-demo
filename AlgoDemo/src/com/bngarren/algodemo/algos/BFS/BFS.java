package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.AbstractAlgoWorker;
import com.bngarren.algodemo.AbstractAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Implementation of the Breadth First Search (BFS) algorithm.
 */
public class BFS extends AbstractAlgorithm<BFS.Worker, BFSController> {

    public BFS(BFSController controller) {
        super(controller);
    }

    @Override
    protected Worker createWorker() {
        return new Worker(controller);
    }

    public static class Worker extends AbstractAlgoWorker<List<IGridLocation>, List<IGridLocation>, BFSController> {

        public Worker(BFSController controller) {
            super(controller);
        }

        @Override
        protected List<IGridLocation> runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException {
            Map<IGridLocation, Cell> cells = controller.getCells();
            Queue<IGridLocation> queue = new LinkedList<>();
            HashSet<IGridLocation> visited = new HashSet<>();
            List<IGridLocation> result = new ArrayList<>();

            IGridLocation startCell = cells.get(GridLocation.of(0, 0));
            queue.add(startCell);
            visited.add(startCell);

            while (!queue.isEmpty()) {
                IGridLocation current = queue.poll();
                result.add(current);

            }

            return result;
        }

        @Override
        protected void processChunk(List<IGridLocation> chunk) {
            if (controller instanceof BFSController c) {
                for (IGridLocation gridLoc : chunk) {
                    c.getView().getCellButton(gridLoc.row(), gridLoc.col()).setColors(Color.YELLOW, Color.BLACK, false);
                }
            }
        }
    }
}
