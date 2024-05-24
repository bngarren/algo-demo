package com.bngarren.algodemo;

/**
 * Abstract base class for algorithms.
 *
 * @param <W> The type of worker used by the algorithm.
 * @param <C> The type of controller used by the algorithm.
 */
public abstract class AbstractAlgorithm<W extends AbstractAlgoWorker<?, ?, C>, C extends IAlgoController<?>> implements IAlgorithm<W, C> {

    protected W worker;
    protected final C controller;

    public AbstractAlgorithm(C controller) {
        this.controller = controller;
    }

    @Override
    public W getWorker() {
        if (worker == null) {
            worker = createWorker();
        }
        return worker;
    }

    @Override
    public void cancel() {
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }
    }

    /**
     * Implemented by concrete algorithm class in order to supply a specific {@link AbstractAlgoWorker}
     *
     * <pre>{@code
     * @Override
     * protected Worker createWorker() {
     *   return new Worker(controller);
     * }
     * }
     * </pre>
     */
    protected abstract W createWorker();

    /**
     * Marker interface for worker packets.
     */
    public interface WorkerPacket {
        // This interface can be extended by specific algorithm implementations to define their worker packets
    }
}
