package com.bngarren.algodemo.util;

import java.util.Objects;

public class GridLocation implements IGridLocation {

    private final int row;
    private final int col;

    public GridLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static GridLocation of(int row, int col) {
        return new GridLocation(row, col);
    }

    @Override
    public int row() {
        return row;
    }

    @Override
    public int col() {
        return col;
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
