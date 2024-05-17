package com.bngarren.algodemo;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Logger;

public abstract class GridAlgoController<T extends GridAlgoView> extends AbstractAlgoController<T> implements FocusListener {

    private static final Logger LOGGER = Logger.getLogger(GridAlgoController.class.getName());
    protected GridAlgoView.CellButton selectedCell;

    protected void updateView() {
        if (view == null) return;

        if (selectedCell == null) {
            view.cellDescriptionTextArea.setText("");
            view.getRootPanel().transferFocus();
        } else {
            @SuppressWarnings("StringBufferReplaceableByString")
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append(selectedCell.getText());
            sb.append("</html>");
            view.cellDescriptionTextArea.setText(sb.toString());
        }

        view.refreshGrid();
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof GridAlgoView.CellButton cell) {
            selectedCell = cell;
            updateView();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof GridAlgoView.CellButton cell) {
            updateView();
        }
    }

}
