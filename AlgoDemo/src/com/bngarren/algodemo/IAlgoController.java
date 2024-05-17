package com.bngarren.algodemo;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

/**
 * Defines a controller that is associated with a specific type of view
 *
 * @param <T> Type of View
 */
public interface IAlgoController<T extends IAlgoView<?>> extends ActionListener, KeyListener, Runnable {

    void setView(T view);

    T getView();

    void run();

    void step();

    void setup();

    void reset();

    boolean shouldStep();
}
