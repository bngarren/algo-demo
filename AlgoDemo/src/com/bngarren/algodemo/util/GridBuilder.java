package com.bngarren.algodemo.util;

import com.bngarren.algodemo.grid.Cell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @param <C> Cell, or subclass of Cell, that composes the grid
 */
public class GridBuilder<C extends Cell> {

    private final int gridSize;
    private final Set<IGridLocation> nonTraversableLocations;
    private final Map<IGridLocation, C> cells;
    private final BiFunction<Integer, Integer, C> cellFactory;

    /**
     * @param gridSize    Size of the grid, N, where N is the length of the row/col (square)
     * @param cellFactory Function that accepts the (row, column) and returns a Cell of type C
     */
    public GridBuilder(int gridSize, BiFunction<Integer, Integer, C> cellFactory) {
        this.gridSize = gridSize;
        this.cellFactory = cellFactory;
        this.nonTraversableLocations = new HashSet<>();
        this.cells = new HashMap<>();
    }

    public GridBuilder<C> addNonTraversable(int row, int col) {
        nonTraversableLocations.add(GridLocation.of(row, col));
        return this;
    }

    public Map<IGridLocation, C> build() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                C cell = cellFactory.apply(i, j);
                if (nonTraversableLocations.contains(cell.toGridLocation())) {
                    cell.setTraversable(false);
                }
                cells.put(GridLocation.of(i, j), cell);
            }
        }
        return cells;
    }
}
