package com.bngarren.algodemo;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class AbstractAlgoWorker<T, V, C extends IAlgoController<?>> extends SwingWorker<T, V> {

    protected final C controller;
    private final Semaphore semaphore;

    public AbstractAlgoWorker(C controller) {
        this.controller = controller;

        // Initialize semaphore with 0 permits so that execution would automatically pause at the first call to semaphore.acquire() unless the UI has called semaphore.release(), e.g. clicking Step
        this.semaphore = new Semaphore(0);
    }

    /**
     * The algorithm to run (in worker thread).
     * <p>
     * The semaphore should be implemented as so:
     * <pre>{@code
     *         if (shouldStep) {
     *             semaphore.acquire()
     *         }
     * }
     * </pre>
     *
     * @param semaphore  Semaphore is used to pause execution of worker thread until released, i.e. by UI thread
     * @param shouldStep If true, should enable the semaphore.
     * @return Returns object of type T
     */
    protected abstract T runAlgorithm(Semaphore semaphore, boolean shouldStep) throws InterruptedException;

    @Override
    protected final T doInBackground() throws Exception {
        return runAlgorithm(semaphore, controller.shouldStep());
    }

    @Override
    protected void process(List<V> chunks) {
        for (V chunk : chunks) {
            processChunk(chunk);
        }
    }

    /**
     * Handles each chunk of type V published from the doInBackground() method (worker thread)
     *
     * @param chunk Object returns from publish()
     */
    protected abstract void processChunk(V chunk);

    /**
     * Releases a permit so that if the worker thread (runAlgorithm() via doInBackground()) is waiting it may continue
     */
    public void release() {
        if (semaphore != null) {
            semaphore.release();
        }
    }

    public void drain() {
        if (semaphore != null) {
            semaphore.drainPermits();
        }
    }

    public void resume() {
        semaphore.release();
    }

}
