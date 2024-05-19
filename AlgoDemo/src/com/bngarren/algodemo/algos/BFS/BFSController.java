package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
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

        initializeCellButtons();

    }

    /**
     * Setups up the view's CellButton's for the first time.
     * <p>
     * Useful for setting appearance of specific CellButtons that pertain to this algorithm, such as the Start cell or End cell
     */
    private void initializeCellButtons() {
        // Since we need to modify the GUI, we schedule this after all other EDT events to ensure the view is initialized and ready
        SwingUtilities.invokeLater(() -> {
            if (view != null && !view.getCellButtons().isEmpty()) {
                updateCellButton(GridLocation.of(0, 0), cellButton -> {
                    cellButton.setColors(START_CELL_BG_COLOR, START_CELL_FG_COLOR, true);
                });

                updateCellButton(GridLocation.of(GRID_SIZE - 1, GRID_SIZE - 1), cellButton -> {
                    cellButton.setColors(END_CELL_BG_COLOR, END_CELL_FG_COLOR, true);
                });

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
        initializeCellButtons();
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
