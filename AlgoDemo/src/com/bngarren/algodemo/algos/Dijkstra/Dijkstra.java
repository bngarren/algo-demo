package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.AbstractAlgoWorker;
import com.bngarren.algodemo.AbstractAlgorithm;
import com.bngarren.algodemo.util.GridOps;
import com.bngarren.algodemo.util.IGridLocation;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Dijkstra extends AbstractAlgorithm<Dijkstra.Worker, DijkstraController> {

    public Dijkstra(DijkstraController controller) {
        super(controller);
    }

    @Override
    protected Worker createWorker() {
        return new Worker(controller);
    }

    public record WorkerPacket(DijkstraCell current, HashMap<IGridLocation, Integer> visited, List<DijkstraCell> queue
    ) {

    }

    public static class Worker extends AbstractAlgoWorker<Set<IGridLocation>, Dijkstra.WorkerPacket, DijkstraController> {


        public Worker(DijkstraController controller) {
            super(controller);
        }

        @Override
        protected Set<IGridLocation> runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException {
            Map<IGridLocation, DijkstraCell> cells = controller.getCells();
            PriorityQueue<DijkstraCell> queue = new PriorityQueue<>(Comparator.comparingInt(DijkstraCell::getDistance));
            HashMap<IGridLocation, Integer> visited = new HashMap<>();
            boolean foundTarget = false;

            DijkstraCell startCell = cells.get(DijkstraController.START_LOC);
            startCell.setDistance(0);
            queue.add(startCell);
            visited.put(startCell.toGridLocation(), 0);

            while (!queue.isEmpty() && !foundTarget) {
                DijkstraCell currentCell = queue.poll();

                List<DijkstraCell> neighbors = GridOps.getNeighbors(currentCell.toGridLocation(), cells);

                for (DijkstraCell neighbor : neighbors) {
                    IGridLocation neighborLoc = neighbor.toGridLocation();
                    if (!visited.containsKey(neighborLoc)) {
                        int currDistance = neighbor.getDistance();
                        if (currDistance > currentCell.getDistance()) {
                            neighbor.setDistance(currentCell.getDistance() + 1);
                            neighbor.setPredecessor(currentCell.toGridLocation());
                        }
                        visited.put(neighborLoc, neighbor.getDistance());

                        if (neighborLoc.equals(DijkstraController.END_LOC)) {
                            // We found the target
                            foundTarget = true;
                            break;
                        }

                        queue.add(neighbor);

                    }
                }

                // Publish intermediate result
                publish(new WorkerPacket(currentCell, visited, queue.stream().toList()));

                if (shouldStep && !foundTarget) {
                    semaphore.acquire();
                }
            }

            // Calculate the shortest path by backtracking from END_LOC to START_LOC
            Set<IGridLocation> path = new LinkedHashSet<>();
            IGridLocation step = DijkstraController.END_LOC;
            while (step != null) {
                path.add(step);
                step = cells.get(step).getPredecessor();
            }

            return path;
        }

        @Override
        protected void onAlgorithmComplete(Set<IGridLocation> result) {
            for (IGridLocation gridLoc : result) {
                controller.updateCellButton(gridLoc, cellButton -> {
                    cellButton.setCurrentColors(Color.GREEN, Color.BLACK);
                });
            }
        }

        @Override
        protected void processChunk(Dijkstra.WorkerPacket packet) {
            controller.resetCellButtonColorsToDefault();


            // update visited
            for (Map.Entry<IGridLocation, Integer> entry : packet.visited().entrySet()) {
                controller.updateCellButton(entry.getKey(), cellButton -> {
                    cellButton.setCurrentColors(Color.GRAY, Color.BLACK);
                    cellButton.setText(String.format("%s%n %d", entry.getKey(), entry.getValue()));
                });
            }

            // update queue
            for (DijkstraCell cell : packet.queue()) {
                controller.updateCellButton(cell, cellButton -> {
                    cellButton.setCurrentColors(Color.PINK, Color.BLACK);
                });
            }

            // update current
            controller.updateCellButton(packet.current, cellButton -> {
                cellButton.setCurrentColors(Color.BLUE, Color.WHITE);
            });
            controller.setSelectedGridLocation(packet.current);

        }
    }
}
