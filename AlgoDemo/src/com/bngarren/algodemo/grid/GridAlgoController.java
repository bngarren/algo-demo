package com.bngarren.algodemo.grid;

import com.bngarren.algodemo.AbstractAlgoController;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

public abstract class GridAlgoController extends AbstractAlgoController<GridAlgoView> {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoController.class.getName());

    protected Map<IGridLocation, Cell> cells;
    /**
     * The greatest of the length of rows and length of cols. Used to render the grid view
     */
    protected int maxGridDimension = 0;
    protected IGridLocation selectedGridLocation;

    public GridAlgoController() {
        this.cells = new HashMap<>();
    }

    /**
     * Implement this method to build the grid, updating the {@link #cells} map.
     */
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

    public void updateCellButton(IGridLocation gridLoc, Consumer<GridAlgoView.CellButton> updater) {
        GridAlgoView.CellButton cellButton = view.getCellButton(gridLoc.row(), gridLoc.col());
        if (cellButton == null) {
            System.out.println("GridAlgoController: Cannot update, cellButton is null @ " + gridLoc);
            return;
        }
        updater.accept(cellButton);
    }

    /**
     * Resets each {@linkplain com.bngarren.algodemo.grid.GridAlgoView.CellButton CellButton} in the {@link GridAlgoView} to its default color scheme.
     * <p>
     * For example, this can be called during the running of an algorithm to reset the colors prior to applying new colors to specific CellButtons depending on the algorithm state
     */
    public void resetCellButtonColorsToDefault() {
        view.getCellButtons().values().forEach(GridAlgoView.CellButton::resetColorsToDefault);
    }

    public Map<IGridLocation, Cell> getCells() {
        return cells;
    }

    public Cell getCell(int row, int col) {
        return cells.get(GridLocation.of(row, col));
    }

    public int getMaxGridDimension() {
        if (cells.isEmpty()) return 0;

        // Return the cached size
        if (maxGridDimension != 0) return maxGridDimension;

        // Otherwise calculate
        int maxRow = cells.keySet().stream().mapToInt(IGridLocation::row).max().orElse(0);
        int maxCol = cells.keySet().stream().mapToInt(IGridLocation::col).max().orElse(0);

        maxGridDimension = Math.max(maxRow, maxCol) + 1; // account for zero-based index
        System.out.printf("GridAlgoController: maxGridDimension is %d%n", maxGridDimension);
        return maxGridDimension;
    }

}
