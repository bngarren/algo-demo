package com.bngarren.algodemo;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * AppKeyEventDispatcher is responsible for globally capturing key events and dispatching them to the currently active KeyListener (i.e. typically the active controller). This allows for centralized key event handling across different components of the application without relying on individual component focus.
 */
public class AppKeyEventDispatcher implements KeyEventDispatcher {

    private KeyListener keyListener;

    public AppKeyEventDispatcher() {
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (keyListener != null) {
            switch (e.getID()) {
                case KeyEvent.KEY_PRESSED -> keyListener.keyPressed(e);
                case KeyEvent.KEY_RELEASED -> keyListener.keyReleased(e);
                case KeyEvent.KEY_TYPED -> keyListener.keyTyped(e);
            }
        }
        return false; // Allow the event to be dispatched to the focused component as well
    }

    /**
     * Returns the currently active KeyListener.
     *
     * @return the current KeyListener
     */
    public KeyListener getKeyListener() {
        return keyListener;
    }

    /**
     * Sets the currently active KeyListener. This listener will receive the following key events:
     * <ul>
     *     <li>KeyEvent.KEY_PRESSED</li>
     *     <li>KeyEvent.KEY_RELEASED</li>
     *     <li>KeyEvent.KEY_TYPED</li>
     * </ul>
     *
     * @param keyListener the KeyListener to set as active
     */
    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }
}
