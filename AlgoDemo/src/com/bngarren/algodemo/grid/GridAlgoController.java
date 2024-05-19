package com.bngarren.algodemo.grid;

import com.bngarren.algodemo.AbstractAlgoController;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class GridAlgoController extends AbstractAlgoController<GridAlgoView> {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoController.class.getName());

    protected Map<IGridLocation, Cell> cells;
    protected int size = 0;
    protected IGridLocation selectedGridLocation;

    public GridAlgoController() {
        this.cells = new HashMap<>();
    }

    protected abstract void initializeCells();

    @Override
    public void setup() {
        initializeCells();
    }

    @Override
    public void reset() {
        super.reset();

        // Reset the GridAlgoView
        view.resetGrid();

        System.out.println("GridAlgoController: Grid reset.");
    }

    protected void updateView() {
        if (view == null) return;

        if (selectedGridLocation == null) {
            view.setCellDescriptionText("");
            view.getRootPanel().transferFocus();
        } else {
            @SuppressWarnings("StringBufferReplaceableByString")
            StringBuilder sb = new StringBuilder();
            sb.append(selectedGridLocation);
            view.setCellDescriptionText(sb.toString());
        }

        view.refreshGrid();
    }

    public Map<IGridLocation, Cell> getCells() {
        return cells;
    }

    public Cell getCell(int row, int col) {
        return cells.get(GridLocation.of(row, col));
    }

    public int getSize() {
        if (cells.isEmpty()) return 0;

        // Return the cached size
        if (size != 0) return size;

        // Otherwise calculate
        int newSize = (int) cells.keySet().stream().filter(k -> k.row() == 0).count();

        // sanity check
        if (Math.sqrt(cells.keySet().size()) != newSize) {
            LOGGER.warning(String.format("Row count %d does not match total size of %d for a square grid", newSize, cells.keySet()
                    .size()));
        }
        size = newSize;
        return newSize;
    }

}
