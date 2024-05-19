package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.grid.GridAlgoView;
import com.bngarren.algodemo.util.GridLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.logging.Logger;

public class BFSController extends GridAlgoController {

    private static final Logger LOGGER = Logger.getLogger(BFSController.class.getName());

    private final static int GRID_SIZE = 10;

    public static final Color START_CELL_BG_COLOR = new Color(20, 200, 10);
    public static final Color START_CELL_FG_COLOR = Color.BLACK;
    public static final Color END_CELL_BG_COLOR = new Color(150, 40, 0);
    public static final Color END_CELL_FG_COLOR = Color.WHITE;

    @Override
    public IAlgorithm<?, ?> getAlgorithm() {
        return Objects.requireNonNullElseGet(algo, () -> new BFS(this));
    }


    @Override
    protected void initializeCells() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cells.put(GridLocation.of(i, j), new Cell(i, j));
            }
        }
        System.out.println("BFSController: cells have initialized cells with size " + cells.size());
    }

    @Override
    public void setup() {

        super.setup();

        // Since we need to modify the GUI, we schedule this after all other EDT events to ensure the view is initialized and ready
        SwingUtilities.invokeLater(() -> {
            if (view != null && !view.getCellButtons().isEmpty()) {
                GridAlgoView.CellButton startCell = view.getCellButton(0, 0);
                startCell.setColors(START_CELL_BG_COLOR, START_CELL_FG_COLOR, true);

                GridAlgoView.CellButton endCell = view.getCellButton(3, 3);
                endCell.setColors(END_CELL_BG_COLOR, END_CELL_FG_COLOR, true);

                view.refreshGrid();
            }
        });

    }

    @Override
    public void step() {
        super.step();
        view.refreshGrid();
    }

    @Override
    public void reset() {
        super.reset();
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
