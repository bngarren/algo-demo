package com.bngarren.algodemo;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * Defines a controller that is associated with a specific type of view
 *
 * @param <V> Type of View
 */
public interface IAlgoController<V extends IAlgoView<?>> extends ActionListener, KeyListener, Runnable {

    IAlgorithm<?, ?> getAlgorithm();

    V getView();

    void run();

    void step();

    /**
     * Perform tasks to set up the state of the controller and initial state of the algorithm.
     * <p>
     * This method should be called during initialization.
     * <p>
     * Any task that reads or modifies the view should be called from {@link #prepareView()} instead.
     */
    void setup();

    void reset();

    boolean shouldStep();
}
