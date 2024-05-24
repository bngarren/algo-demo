package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.util.GridBuilder;

import java.awt.*;
import java.util.Objects;

public class DijkstraController extends GridAlgoController<Cell> {

    private final static int GRID_SIZE = 10;

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
