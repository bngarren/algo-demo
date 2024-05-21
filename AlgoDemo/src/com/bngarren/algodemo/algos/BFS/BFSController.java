package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class BFSController extends GridAlgoController {

    private static final Logger LOGGER = Logger.getLogger(BFSController.class.getName());

    private final static int GRID_SIZE = 5;

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

        Set<IGridLocation> nonTraversable = new HashSet<>();
        nonTraversable.add(GridLocation.of(2, 0));
        nonTraversable.add(GridLocation.of(2, 1));
        nonTraversable.add(GridLocation.of(2, 2));
        nonTraversable.add(GridLocation.of(2, 3));
        nonTraversable.add(GridLocation.of(2, 4));

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Cell cell = new Cell(i, j);
                if (nonTraversable.contains(cell.toGridLocation())) {
                    cell.setTraversable(false);
                }
                cells.put(GridLocation.of(i, j), cell);
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

                // START CELL
                updateCellButton(GridLocation.of(0, 0), cellButton -> {
                    cellButton.setDefaultColors(START_CELL_BG_COLOR, START_CELL_FG_COLOR, true);
                });

                // END CELL
                updateCellButton(GridLocation.of(GRID_SIZE - 1, GRID_SIZE - 1), cellButton -> {
                    cellButton.setDefaultColors(END_CELL_BG_COLOR, END_CELL_FG_COLOR, true);
                });

                // NON-TRAVERSABLE CELLS
                cells.values()
                        .stream()
                        .filter(c -> !c.isTraversable())
                        .forEach(nt -> updateCellButton(nt, cellButton -> {
                            cellButton.setDefaultColors(Color.BLACK, Color.WHITE, true);
                        }));

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


}
