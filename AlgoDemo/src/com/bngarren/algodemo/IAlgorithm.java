package com.bngarren.algodemo;

/**
 * Interface representing an algorithm.
 *
 * @param <W> The type of worker used by the algorithm.
 * @param <C> The type of controller used by the algorithm.
 */
public interface IAlgorithm<W extends AbstractAlgoWorker<?, ?, C>, C extends IAlgoController<?>> {

    /**
     * Returns an {@linkplain AbstractAlgoWorker} (a custom {@link javax.swing.SwingWorker} class) that handles execution of the algorithm.
     *
     * @return Instance of AbstractAlgoWorker
     */
    W getWorker();

    /**
     * Cancels the current worker and sets it to null.
     * <p>
     * The next call to {@link #getWorker()} will create a new instance.
     */
    void cancel();

}
