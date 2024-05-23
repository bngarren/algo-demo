package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.IAlgorithm;
import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.grid.GridAlgoController;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DijkstraController extends GridAlgoController {

    private final static int GRID_SIZE = 10;


    @Override
    public IAlgorithm<?, ?> getAlgorithm() {
        return Objects.requireNonNullElseGet(algo, () -> new Dijkstra(this));
    }

    @Override
    protected void initializeCells() {

        Set<IGridLocation> nonTraversable = new HashSet<>();
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
    }

    @Override
    protected void initializeCellButtons() {

    }
}
