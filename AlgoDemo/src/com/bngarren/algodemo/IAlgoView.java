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
     * Called after the controller has been added and the controller's setup/initialization is complete. This allows the view to compute and render the GUI based on members of the controller.
     * <p>
     * This method should be called during the view/constructor creation and setup phase, AFTER the controller initialization is complete:
     * <pre>{@code
     * V view = viewFactory.get();
     * C controller = controllerFactory.get();
     * controller.setView(view);
     * view.setController(controller);
     * controller.setup();
     * view.onControllerReady(controller); // <-- HERE
     * }
     * </pre>
     *
     * @param controller The controller backing this view
     */
    void onControllerReady(T controller);
}
