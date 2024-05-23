package com.bngarren.algodemo.algos.BFS;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.util.GridBuilder;
import com.bngarren.algodemo.util.GridLocation;

import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

public class BFSController extends GridAlgoController<Cell> {

    private static final Logger LOGGER = Logger.getLogger(BFSController.class.getName());

    private final static int GRID_SIZE = 5;

    public static final Color START_CELL_BG_COLOR = new Color(20, 200, 10);
    public static final Color START_CELL_FG_COLOR = Color.BLACK;
    public static final Color END_CELL_BG_COLOR = new Color(150, 40, 0);
    public static final Color END_CELL_FG_COLOR = Color.WHITE;

    public BFSController(BFSView view) {
        super(view);
        registerViewSetupTask(this::initializeCellButtons);
    }

    @Override
    public IAlgorithm<?, ?> getAlgorithm() {
        return Objects.requireNonNullElseGet(algo, () -> new BFS(this));
    }


    @Override
    protected void initializeCells() {
        cells = new GridBuilder<>(GRID_SIZE, Cell::new)
                .addNonTraversable(2, 0)
                .addNonTraversable(2, 1)
                .addNonTraversable(2, 2)
                .addNonTraversable(2, 3)
                .addNonTraversable(2, 4)
                .build();
    }


    private void initializeCellButtons() {
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

    }


}
