package com.bngarren.algodemo;

import com.bngarren.algodemo.util.ICell;
import com.bngarren.algodemo.util.SimpleCell;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class GridAlgoView extends AbstractAlgoView<GridAlgoController<GridAlgoView>> {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoView.class.getName());

    private static final int DEFAULT_GRID_SIZE = 10;
    private static final int DEFAULT_CELL_SIZE = 90;


    protected final int size;
    protected JPanel gridPanel;

    protected Map<ICell, CellButton> cells;

    BufferedImage cellOverlayTexture;

    public GridAlgoView() {
        this(DEFAULT_GRID_SIZE);
    }

    public GridAlgoView(int size) {
        this.size = size;
        cells = new HashMap<>();
        setupGrid();

        // Load cell texture
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("assets/cellOverlayCheckered.png")) {
            if (is != null) {
                cellOverlayTexture = ImageIO.read(is);
            } else {
                LOGGER.severe("Texture image not found");
            }
        } catch (IOException e) {
            LOGGER.severe("Failed to load texture: " + e.getMessage());
        }
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

    /**
     * A button representing a cell in a grid.
     * Implements ICell interface to provide row and column information.
     */
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
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            gac.updateView();
                        }
                    });
                }
            });

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Custom background for selected cell
            if (controller.selectedCell == this) {
                if (cellOverlayTexture != null) {
                    g.drawImage(cellOverlayTexture, 0, 0, getWidth(), getHeight(), null);
                }
            }
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
