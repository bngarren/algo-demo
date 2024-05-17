package com.bngarren.algodemo.util;

import java.util.Objects;

public record SimpleCell(int row, int col) implements ICell {

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ICell other)) {
            return false;
        }
        return this.row == other.row() && this.col == other.col();
    }
}
