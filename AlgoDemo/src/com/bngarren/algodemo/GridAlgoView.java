package com.bngarren.algodemo;

import com.bngarren.algodemo.util.ICell;
import com.bngarren.algodemo.util.SimpleCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

public abstract class GridAlgoView extends AbstractAlgoView<GridAlgoController<GridAlgoView>> {

    private static final int DEFAULT_GRID_SIZE = 10;
    private static final int DEFAULT_CELL_SIZE = 90;

    protected final int size;
    protected JPanel gridPanel;

    protected Map<ICell, CellButton> cells;

    public GridAlgoView() {
        this(DEFAULT_GRID_SIZE);
    }

    public GridAlgoView(int size) {
        this.size = size;
        cells = new HashMap<>();
        setupGrid();
    }

    private void setupGrid() {
        gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(1, 1, 1, 1); // Add some space between buttons

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                SimpleCell cell = new SimpleCell(i, j);
                GridAlgoView.CellButton cellButton = new GridAlgoView.CellButton(i, j, DEFAULT_CELL_SIZE);
                cellButton.addActionListener(cellButton);
                cells.put(cell, cellButton);
                gbc.gridx = j;
                gbc.gridy = i;
                gridPanel.add(cellButton, gbc);
            }
        }
        rootPanel.add(gridPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::resizeCells);

    }

    private void resizeCells() {
        Dimension parentSize = rootPanel.getSize();
        if (parentSize.width == 0 || parentSize.height == 0) {
            return; // Skip resize if the parent size is not yet determined
        }
        int maxCellSizeWidth = parentSize.width / size;
        int maxCellSizeHeight = parentSize.height / size;
        int cellSize = Math.min(DEFAULT_CELL_SIZE, Math.min(maxCellSizeWidth, maxCellSizeHeight) - 10);

        for (CellButton cellButton : cells.values()) {
            cellButton.setCellSize(cellSize);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public void refreshGrid() {
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public CellButton getCell(int row, int col) {
        return cells.get(new SimpleCell(row, col));
    }

    public void setCell(int row, int col, CellButton cellButton) {
        cells.put(new SimpleCell(row, col), cellButton);
    }

    public Map<ICell, CellButton> getCells() {
        return cells;
    }

    public class CellButton extends JButton implements ICell, ActionListener {

        int row;
        int col;
        int cellSize;
        Color bg = Color.WHITE;
        Color fg = Color.BLACK;

        public CellButton(int row, int col, int cellSize) {
            this.row = row;
            this.col = col;
            setCellSize(cellSize);
            setColors(bg, fg, true);
            setText(String.format("(%d,%d)", row, col));
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);

            SwingUtilities.invokeLater(() -> {
                if (controller instanceof GridAlgoController<?> gac) {
                    addFocusListener(new FocusListener() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            gac.selectedCell = CellButton.this;
                            gac.updateView();

                            setColors(Color.ORANGE, fg, false);
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            gac.updateView();
                            setColors(bg, fg, false);
                        }
                    });
                }
            });

        }

        public void setColors(Color bg, Color fg, boolean newDefault) {
            setBackground(bg);
            setForeground(fg);

            if (newDefault) {
                this.bg = bg;
                this.fg = fg;
            }
        }

        public void setCellSize(int cellSize) {
            this.cellSize = cellSize;
            setMinimumSize(new Dimension(cellSize, cellSize));
            setPreferredSize(new Dimension(cellSize, cellSize));
            setMaximumSize(new Dimension(cellSize, cellSize));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.printf("Button action @ (%d,%d), cellSize=%d %n", this.row, this.col, this.cellSize);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(cellSize, cellSize);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(cellSize, cellSize);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(cellSize, cellSize);
        }


        @Override
        public int row() {
            return row;
        }

        @Override
        public int col() {
            return col;
        }
    }
}
