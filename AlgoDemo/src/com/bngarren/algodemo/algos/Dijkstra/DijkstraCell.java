package com.bngarren.algodemo.algos.Dijkstra;

import com.bngarren.algodemo.grid.Cell;
import com.bngarren.algodemo.util.IGridLocation;

public class DijkstraCell extends Cell {

    private int distance;
    private IGridLocation predecessor;

    public DijkstraCell(int row, int col) {
        super(row, col);
        this.distance = Integer.MAX_VALUE;
        this.predecessor = null;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public IGridLocation getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(IGridLocation predecessor) {
        this.predecessor = predecessor;
    }
}
