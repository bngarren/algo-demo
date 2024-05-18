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

    void setView(V view);

    V getView();

    void run();

    void step();

    void setup();

    void reset();

    boolean shouldStep();
}
