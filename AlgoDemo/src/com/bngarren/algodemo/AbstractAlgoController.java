package com.bngarren.algodemo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Provides a generic way to associate the controller with a specific view
 *
 * @param <V> Type of View
 */
public abstract class AbstractAlgoController<V extends IAlgoView<?>> implements IAlgoController<V> {

    protected IAlgorithm<?, ?> algo;
    protected V view;
    protected boolean shouldStep;
    protected int stepCount = 0;

    public AbstractAlgoController() {
        super();
        algo = getAlgorithm();
        this.shouldStep = false;
    }

    @Override
    public void run() {
        System.out.println("Running algo!");
        shouldStep = false;
        algo.reset();
        algo.getWorker().execute();
    }

    @Override
    public void reset() {
        algo.reset();
    }

    /**
     * Ensure that super is called if you override!
     */
    @Override
    public void step() {
        boolean firstStep = stepCount == 0;
        shouldStep = true;

        algo.getWorker().execute();

        if (algo.getWorker().getState() == SwingWorker.StateValue.STARTED) {
            if (!firstStep) algo.getWorker().release();
        }

        stepCount++;
    }

    @Override
    public boolean shouldStep() {
        return shouldStep;
    }

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE -> run();
            case KeyEvent.VK_PERIOD -> step();
            case KeyEvent.VK_R -> reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
