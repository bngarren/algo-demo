package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.GridAlgoController;
import com.bngarren.algodemo.GridAlgoView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class BFSController extends GridAlgoController<GridAlgoView> {

    public static final Color START_CELL_BG_COLOR = new Color(20, 200, 10);
    public static final Color START_CELL_FG_COLOR = Color.BLACK;
    public static final Color END_CELL_BG_COLOR = new Color(150, 40, 0);
    public static final Color END_CELL_FG_COLOR = Color.WHITE;

    BFS algo;

    public BFSController() {
        super();
    }

    @Override
    public void run() {
        System.out.println("Running algo!");
        shouldStep = false;
        algo = new BFS(this);
        algo.execute();
    }

    @Override
    public void step() {
        boolean firstStep = false;
        shouldStep = true;
        if (algo == null) {
            algo = new BFS(this);
            algo.execute();
            firstStep = true;
        }
        if (algo.getState() == SwingWorker.StateValue.STARTED) {
            if (!firstStep) algo.release();
        }
        view.refreshGrid();
    }

    @Override
    public void setup() {
        GridAlgoView.CellButton startCell = view.getCell(0, 0);
        startCell.setColors(START_CELL_BG_COLOR, START_CELL_FG_COLOR, true);

        GridAlgoView.CellButton endCell = view.getCell(3, 3);
        endCell.setColors(END_CELL_BG_COLOR, END_CELL_FG_COLOR, true);

        view.refreshGrid();
    }

    @Override
    public void reset() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
