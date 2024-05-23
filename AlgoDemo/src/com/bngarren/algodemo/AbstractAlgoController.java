package com.bngarren.algodemo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a generic way to associate the controller with a specific view
 *
 * @param <V> Type of View
 */
public abstract class AbstractAlgoController<V extends IAlgoView<?>> implements IAlgoController<V> {

    protected IAlgorithm<?, ?> algo;
    protected V view;

    /**
     * A view setup task runs after the view has been created and initialized. Such a task can be used to customize the initial view for a specific algorithm.
     */
    private final List<Runnable> viewSetupTasks;
    protected boolean shouldStep;
    protected int stepCount = 0;

    public AbstractAlgoController(V view) {
        this.view = view;
        algo = getAlgorithm();
        this.viewSetupTasks = new ArrayList<>();
        this.shouldStep = false;
    }

    @Override
    public final void setup() {
        initialize();
        initializeCommon();
        prepareView();
    }

    /**
     * Implement custom setup logic that runs prior to view setup.
     * <p>
     * This is where algorithm-specific setup should be implemented, e.g.:
     * <ul>
     *     <li>Initializing the logical cells in a grid or the nodes of a graph</li>
     * </ul>
     */
    protected abstract void initialize();

    /**
     * Initialization that is common to all subclasses.
     * <p>
     * If overridden, must call {@code super.initializeCommon()} last.
     */
    protected void initializeCommon() {
        view.onControllerReady();
    }

    /**
     * Perform tasks related to modifying the view for initial presentation.
     * <p>
     * This method is called after this controller has initialized and completed all tasks in {@link #setup()}. This method is also called when the controller is {@linkplain IAlgoController#reset() reset}.
     * <p>
     * Rather than overriding this method, {@linkplain #registerViewSetupTask(Runnable) add tasks} to {@link #viewSetupTasks} so that they will be run via {@link SwingUtilities#invokeLater(Runnable)}.
     */
    protected void prepareView() {
        runAllViewSetupTasks();
    }

    /**
     * Runs all queued view initialization tasks via {@linkplain SwingUtilities#invokeLater(Runnable)}
     */
    private void runAllViewSetupTasks() {
        SwingUtilities.invokeLater(() -> {
            viewSetupTasks.forEach(Runnable::run);
        });
    }

    /**
     * Adds the given task to the queue that will run when the view needs to be prepared.
     */
    protected void registerViewSetupTask(Runnable task) {
        viewSetupTasks.add(task);
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
