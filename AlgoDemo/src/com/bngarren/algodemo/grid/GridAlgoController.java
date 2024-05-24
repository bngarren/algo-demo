package com.bngarren.algodemo.grid;

import com.bngarren.algodemo.AbstractAlgoController;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.GridLocationHistory;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @param <C> Cell, or subclass of Cell, that makes up the grid
 */
public abstract class GridAlgoController<C extends Cell> extends AbstractAlgoController<GridAlgoView> {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoController.class.getName());

    /**
     * The logical {@linkplain Cell Cells} backing the grid and algorithm.
     * <p>
     * These should be created by implementing {@link #initializeCells()} in a concrete class.
     */
    protected Map<IGridLocation, C> cells;
    /**
     * The greatest of the length of rows and length of cols. Used to render the grid view
     */
    protected int maxGridDimension = 0;

    /**
     * A {@link GridLocationHistory} object that stores the currently selected and previously selected grid location (should correspond to a {@link Cell} and {@link com.bngarren.algodemo.grid.GridAlgoView.CellButton}.
     */
    protected GridLocationHistory selectedGridLocation;

    public GridAlgoController(GridAlgoView view) {
        super(view);
        this.cells = new HashMap<>();
        this.selectedGridLocation = new GridLocationHistory(2);
    }

    @Override
    protected void initialize() {
        initializeCells();
    }


    /**
     * Implement this method to build the grid, updating the {@link #cells} map.
     */
    protected abstract void initializeCells();


    @Override
    public void reset() {
        // Reset the GridAlgoView, i.e. clear the grid and set it up again
        view.resetGrid();

        // Clear any selected grid loc and update GUI to reflect this
        selectedGridLocation.clear();
        updateView();

        // AbstractAlgoController.reset() will cancel any currently running algo worker and call prepareView()
        super.reset();
    }

    protected void updateView() {
        if (view == null) return;


        // Update GUI based on the presence/absence of a selected grid location (selected cell)
        selectedGridLocation.getCurrent().ifPresentOrElse(selected -> {
            @SuppressWarnings("StringBufferReplaceableByString")
            StringBuilder sb = new StringBuilder();
            sb.append(getView().getCellButton(selected.row(), selected.col()).getText());
            view.setCellDescriptionText(sb.toString());
        }, () -> {
            view.setCellDescriptionText("");
            view.setAlgoDescriptionText(view.getDescription().isEmpty() ? "" : view.getDescription());
        });

    }

    public void updateCellButton(IGridLocation gridLoc, Consumer<GridAlgoView.CellButton> updater) {
        GridAlgoView.CellButton cellButton = view.getCellButton(gridLoc.row(), gridLoc.col());
        if (cellButton == null) {
            System.out.println("GridAlgoController: Cannot update, cellButton is null @ " + gridLoc);
            return;
        }
        updater.accept(cellButton);
        updateView();
    }

    /**
     * Resets each {@linkplain com.bngarren.algodemo.grid.GridAlgoView.CellButton CellButton} in the {@link GridAlgoView} to its default color scheme.
     * <p>
     * For example, this can be called during the running of an algorithm to reset the colors prior to applying new colors to specific CellButtons depending on the algorithm state
     */
    public void resetCellButtonColorsToDefault() {
        view.getCellButtons().values().forEach(GridAlgoView.CellButton::resetColorsToDefault);
    }

    public Map<IGridLocation, C> getCells() {
        return cells;
    }

    public C getCell(int row, int col) {
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

    public Optional<IGridLocation> getCurrentSelectedGridLocation() {
        return selectedGridLocation.getCurrent();
    }

    public Optional<IGridLocation> getPrevSelectedGridLocation() {
        return selectedGridLocation.getPrevious();
    }

    public void setSelectedGridLocation(IGridLocation selectedGridLocation) {
        this.selectedGridLocation.push(selectedGridLocation);
        this.selectedGridLocation.getPrevious().ifPresent(prev -> view.getCellButton(prev.row(), prev.col()).repaint());
        updateView();
    }

    /**
     * Requests focus for the CellButton at the given grid location
     */
    public void focusCellAt(IGridLocation gridLoc) {
        view.getCellButton(gridLoc.row(), gridLoc.col()).requestFocus();
    }

}
