package com.bngarren.algodemo;

import javax.swing.*;

/**
 * Defines a view that is associated with a specific type of controller
 *
 * @param <T> Type of controller
 */
public interface IAlgoView<T extends IAlgoController<?>> {

    /**
     * The title used for the UI representation of this IAlgoView.
     */
    String getTitle();

    /**
     * Returns the main/parent panel that hosts this IAlgoView
     */
    JPanel getRootPanel();

    void setController(T controller);

    /**
     * Allows the view to compute and render the GUI based on initialized members in the controller.
     * <p>
     * This method should be called AFTER:
     * <ol>
     *     <li>Controller setup is complete</li>
     *     <li>{@link #setController(IAlgoController)} has been called on this view</li>
     * </ol>
     */
    void onControllerReady();
}
