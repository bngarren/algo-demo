package com.bngarren.algodemo;

import javax.swing.*;

/**
 * Defines a view that is associated with a specific type of controller
 *
 * @param <T> Type of controller
 */
public interface IAlgoView<T extends IAlgoController<?>> {

    String getTitle();

    JPanel getRootPanel();

    void setController(T controller);

    void onControllerReady(T controller);
}
