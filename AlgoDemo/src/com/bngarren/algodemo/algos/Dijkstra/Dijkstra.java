package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.AbstractAlgoWorker;
import com.bngarren.algodemo.AbstractAlgorithm;
import com.bngarren.algodemo.algos.BFS.BFSController;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Dijkstra extends AbstractAlgorithm<Dijkstra.Worker, DijkstraController> {

    public Dijkstra(DijkstraController controller) {
        super(controller);
    }

    public record WorkerPacket(IGridLocation current, List<IGridLocation> neighbors, List<IGridLocation> visited,
                               List<IGridLocation> queue) {

    }

    public static class Worker extends AbstractAlgoWorker<List<IGridLocation>, Dijkstra.WorkerPacket, BFSController> {

        public Worker(BFSController controller) {
            super(controller);
        }

        @Override
        protected List<IGridLocation> runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException {


        }

        @Override
        protected void processChunk(Dijkstra.WorkerPacket packet) {

            controller.resetCellButtonColorsToDefault();


        }
    }
}
