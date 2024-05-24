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

    private static List<Cell> getNeighbors(IGridLocation current, Map<IGridLocation, Cell> cells) {

        List<Cell> result = new ArrayList<>();
        int[][] mvmts = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] mvmt : mvmts) {
            GridLocation next = new GridLocation(current.row() + mvmt[0], current.col() + mvmt[1]);
            if (cells.containsKey(next)) {
                Cell neighbor = cells.get(next);
                if (neighbor.isTraversable()) {
                    result.add(neighbor);
                }
            }
        }
        return result;
    }

    public record WorkerPacket(IGridLocation current, List<IGridLocation> neighbors, List<IGridLocation> visited,
                               List<IGridLocation> queue) {

    }

    public static class Worker extends AbstractAlgoWorker<List<IGridLocation>, WorkerPacket, BFSController> {

        public Worker(BFSController controller) {
            super(controller);
        }

        @Override
        protected List<IGridLocation> runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException {
            Map<IGridLocation, Cell> cells = controller.getCells();
            Queue<IGridLocation> queue = new LinkedList<>();
            HashSet<IGridLocation> visited = new HashSet<>();
            List<IGridLocation> result = new ArrayList<>();

            IGridLocation startCell = GridLocation.of(0, 0);
            queue.add(startCell);
            visited.add(startCell);

            while (!queue.isEmpty()) {
                IGridLocation current = queue.poll();
                result.add(current);

                List<Cell> neighbors = getNeighbors(current, cells);
                List<IGridLocation> neighborLocations = new ArrayList<>();

                for (Cell neighbor : neighbors) {
                    GridLocation neighborLoc = neighbor.toGridLocation();
                    if (!visited.contains(neighborLoc)) {
                        queue.add(neighborLoc);
                        visited.add(neighborLoc);
                        neighborLocations.add(neighborLoc);
                    }
                }

                // Publish intermediate result
                publish(new WorkerPacket(current, neighborLocations, visited.stream().toList(), queue.stream()
                        .toList()));

                if (shouldStep) {
                    semaphore.acquire();
                }
            }

            return result;
        }

        @Override
        protected void processChunk(WorkerPacket packet) {

            controller.resetCellButtonColorsToDefault();

            IGridLocation current = packet.current();
            List<IGridLocation> neighbors = packet.neighbors();
            List<IGridLocation> visited = packet.visited();
            List<IGridLocation> queue = packet.queue();

            // update visited
            for (IGridLocation v : visited) {
                controller.updateCellButton(v, cellButton -> {
                    cellButton.setCurrentColors(Color.GRAY, Color.BLACK);
                });
            }

            // update queue
            StringBuilder sb = new StringBuilder("Queue\n");
            for (IGridLocation q : queue) {
                sb.append(q).append("\n");
                controller.updateCellButton(q, cellButton -> {
                    cellButton.setCurrentColors(Color.PINK, Color.BLACK);
                });
            }

            // update current
            controller.updateCellButton(current, cellButton -> {
                cellButton.setCurrentColors(Color.BLUE, Color.WHITE);
            });
            controller.setSelectedGridLocation(current);

            // update neighbors
            for (IGridLocation neighbor : neighbors) {
                controller.updateCellButton(neighbor, cellButton -> {
                    cellButton.setCurrentColors(Color.CYAN, Color.BLACK);
                });
            }

            controller.getView().setAlgoDescriptionText(sb.toString());

        }
    }
}
