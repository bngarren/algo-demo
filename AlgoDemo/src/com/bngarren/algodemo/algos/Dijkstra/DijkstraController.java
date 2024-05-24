package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.util.GridBuilder;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.awt.*;
import java.util.Objects;

public class DijkstraController extends GridAlgoController<DijkstraCell> {

    private final static int GRID_SIZE = 8;
    public final static IGridLocation START_LOC = GridLocation.of(0, 0);
    public final static IGridLocation END_LOC = GridLocation.of(GRID_SIZE - 1, GRID_SIZE - 1);

    public static final Color START_CELL_BG_COLOR = new Color(20, 200, 10);
    public static final Color START_CELL_FG_COLOR = Color.BLACK;
    public static final Color END_CELL_BG_COLOR = new Color(150, 40, 0);
    public static final Color END_CELL_FG_COLOR = Color.WHITE;

    public DijkstraController(DijkstraView view) {
        super(view);
        registerViewSetupTask(this::initializeCellButtons);
    }

    @Override
    public IAlgorithm<?, ?> getAlgorithm() {
        return Objects.requireNonNullElseGet(algo, () -> new Dijkstra(this));
    }

    @Override
    protected void initializeCells() {
        cells = new GridBuilder<>(GRID_SIZE, DijkstraCell::new)
                .addNonTraversable(2, 0)
                .addNonTraversable(2, 1)
                .addNonTraversable(2, 2)
                .addNonTraversable(2, 3)
                .addNonTraversable(2, 4)
                .addNonTraversable(4, 4)
                .addNonTraversable(4, 5)
                .build();
    }

    private void initializeCellButtons() {
        if (view != null && !view.getCellButtons().isEmpty()) {

            // START CELL
            updateCellButton(START_LOC, cellButton -> {
                cellButton.setDefaultColors(START_CELL_BG_COLOR, START_CELL_FG_COLOR, true);
            });

            // END CELL
            updateCellButton(END_LOC, cellButton -> {
                cellButton.setDefaultColors(END_CELL_BG_COLOR, END_CELL_FG_COLOR, true);
            });


            // NON-TRAVERSABLE CELLS
            cells.values()
                    .stream()
                    .filter(c -> !c.isTraversable())
                    .forEach(nt -> updateCellButton(nt, cellButton -> {
                        cellButton.setDefaultColors(Color.BLACK, Color.WHITE, true);
                    }));

//            view.refreshGrid();
        }
    }
}
