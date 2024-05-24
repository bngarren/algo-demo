package com.bngarren.algodemo.util;

import com.bngarren.algodemo.grid.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridOps {

    private GridOps() {
    }

    public static <C extends Cell> List<C> getNeighbors(IGridLocation current, Map<IGridLocation, C> cells) {

        List<C> result = new ArrayList<>();
        int[][] mvmts = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] mvmt : mvmts) {
            GridLocation next = new GridLocation(current.row() + mvmt[0], current.col() + mvmt[1]);
            if (cells.containsKey(next)) {
                C neighbor = cells.get(next);
                if (neighbor.isTraversable()) {
                    result.add(neighbor);
                }
            }
        }
        return result;
    }
}
