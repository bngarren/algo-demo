package com.bngarren.algodemo.grid;

import com.bngarren.algodemo.AbstractAlgoController;
import com.bngarren.algodemo.AbstractAlgoView;
import com.bngarren.algodemo.util.GridLocation;
import com.bngarren.algodemo.util.IGridLocation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class GridAlgoView extends AbstractAlgoView<GridAlgoController<?>> {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoView.class.getName());

    private static final int DEFAULT_CELL_SIZE = 90;

    protected JPanel gridPanel;

    /**
     * {@linkplain CellButton CellButtons} are the UI representation of the logical {@linkplain Cell Cells} that form the grid
     */
    protected Map<IGridLocation, CellButton> cellButtons;

    /**
     * Texture applied to the selected Cell
     */
    BufferedImage cellOverlayTexture;

    public GridAlgoView() {
        cellButtons = new HashMap<>();

        loadTextures();
    }

    @Override
    public void onControllerReady() {
        super.onControllerReady();
        setupGrid();
    }

    private void setupGrid() {

        if (controller == null) {
            LOGGER.warning("Cannot setupGrid before controller is added/initialized.");
            return;
        }

        gridPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(1, 1, 1, 1); // Add some space between buttons

        for (Cell cell : controller.getCells().values()) {
            int row = cell.row();
            int col = cell.col();
            GridAlgoView.CellButton cellButton = new GridAlgoView.CellButton(cell, DEFAULT_CELL_SIZE);
            cellButton.addActionListener(cellButton);
            cellButtons.put(cell.toGridLocation(), cellButton);
            gbc.gridx = col;
            gbc.gridy = row;
            gridPanel.add(cellButton, gbc);
        }

        // Sets the center of the root panel to this grid panel
        setCenterPanel(gridPanel);

        System.out.printf("GridAlgoView: cellButtons have been initialized with size %d%n", cellButtons.size());

        SwingUtilities.invokeLater(this::resizeCells);

    }

    private void resizeCells() {

        if (controller == null) {
            LOGGER.warning("Cannot setupGrid before controller is added/initialized.");
            return;
        }

        if (controller.getMaxGridDimension() == 0) {
            return;
        }

        Dimension parentSize = getRootPanel().getSize();
        if (parentSize.width == 0 || parentSize.height == 0) {
            return; // Skip resize if the parent size is not yet determined
        }
        int maxCellSizeWidth = parentSize.width / controller.getMaxGridDimension();
        int maxCellSizeHeight = parentSize.height / controller.getMaxGridDimension();
        int cellSize = Math.min(DEFAULT_CELL_SIZE, Math.min(maxCellSizeWidth, maxCellSizeHeight) - 10);

        for (CellButton cellButton : cellButtons.values()) {
            cellButton.setCellSize(cellSize);
        }

        refreshGrid();
    }

    private void loadTextures() {
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

    /**
     * Removes/clears grid panel and cell buttons and sets up the grid again
     */
    public void resetGrid() {
        gridPanel.removeAll();
        cellButtons.clear();
        setupGrid();
    }

    /**
     * Revalidate and repaint <i>only</i> the grid panel.
     * <p>
     * Consider calling {@link AbstractAlgoController#refreshView()} if the entire view should be repainted/revalidated.
     */
    public void refreshGrid() {
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public CellButton getCellButton(int row, int col) {
        return cellButtons.get(GridLocation.of(row, col));
    }

    public Map<IGridLocation, CellButton> getCellButtons() {
        return cellButtons;
    }

    /**
     * A button representing a cell in a grid.
     * Implements ICell interface to provide row and column information.
     */
    public class CellButton extends JButton implements IGridLocation, ActionListener {

        Cell cell;
        int cellSize;

        Color defaultBg = Color.WHITE;
        Color defaultFg = Color.BLACK;

        public CellButton(Cell cell, int cellSize) {
            this.cell = cell;
            setCellSize(cellSize);
            setCurrentColors(defaultBg, defaultFg);
            setText(String.format("(%d,%d)", cell.row(), cell.col()));
            setOpaque(true);
            setBorderPainted(false);
            setFocusPainted(false);
            addActionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Custom background for selected cell
            controller.getCurrentSelectedGridLocation().ifPresent(selected -> {
                if (selected.equals(cell.toGridLocation())) {
                    if (cellOverlayTexture != null) {
                        g.drawImage(cellOverlayTexture, 0, 0, getWidth(), getHeight(), null);
                    }
                }
            });
        }

        public void setDefaultColors(Color bg, Color fg, boolean updateCurrent) {
            this.defaultBg = bg;
            this.defaultFg = fg;
            if (updateCurrent) {
                setCurrentColors(bg, fg);
            }
        }


        public void setCurrentColors(Color bg, Color fg) {
            setBackground(bg);
            setForeground(fg);
        }

        /**
         * Sets this CellButton's background and foreground colors back to default
         */
        public void resetColorsToDefault() {
            setCurrentColors(defaultBg, defaultFg);
        }

        public void setCellSize(int cellSize) {
            this.cellSize = cellSize;
            setMinimumSize(new Dimension(cellSize, cellSize));
            setPreferredSize(new Dimension(cellSize, cellSize));
            setMaximumSize(new Dimension(cellSize, cellSize));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            controller.setSelectedGridLocation(cell.toGridLocation());
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
            return cell.row();
        }

        @Override
        public int col() {
            return cell.col();
        }
    }
}
