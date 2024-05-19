package com.bngarren.algodemo.grid;

import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import java.util.Objects;

public class Cell implements IGridLocation {

    protected int row;
    protected int col;
    protected boolean traversable;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.traversable = true;
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int col() {
        return col;
    }

    public boolean isAt(int row, int col) {
        return this.row == row && this.col == col;
    }

    public boolean isAt(IGridLocation gridLoc) {
        return isAt(gridLoc.row(), gridLoc.col());
    }

    public boolean isTraversable() {
        return traversable;
    }

    public void setTraversable(boolean val) {
        traversable = val;
    }

    public GridLocation toGridLocation() {
        return GridLocation.of(row, col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof IGridLocation other)) {
            return false;
        }
        return this.row == other.row() && this.col == other.col();
    }
}
