package com.bngarren.algodemo;

import javax.swing.*;

/**
 * Provides a generic way to associate the controller with a specific view
 *
 * @param <T> Type of View
 */
public abstract class AbstractAlgoController<T extends IAlgoView<?>> implements IAlgoController<T> {

    protected T view;
    protected boolean shouldStep;

    public AbstractAlgoController() {
        super();
        this.shouldStep = false;
        SwingUtilities.invokeLater(this::setup);
    }

    @Override
    public boolean shouldStep() {
        return shouldStep;
    }

    @Override
    public T getView() {
        return view;
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }
}
