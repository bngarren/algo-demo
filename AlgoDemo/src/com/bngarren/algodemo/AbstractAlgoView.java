package com.bngarren.algodemo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class AbstractAlgoView<T extends IAlgoController<?>> implements IAlgoView<T> {

    protected JPanel rootPanel;
    protected JPanel eastPanel;

    protected JTextArea cellDescriptionText;
    protected JTextArea algoDescriptionText;
    protected JButton runButton;
    protected JButton stepButton;
    protected T controller;

    public AbstractAlgoView() {
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Setup East panel with preferred size
        eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(400, 0));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        cellDescriptionText = new JTextArea();
        cellDescriptionText.setEditable(false);
        JScrollPane cellScrollPane = new JScrollPane(cellDescriptionText);
        cellScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        cellScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        algoDescriptionText = new JTextArea();
        algoDescriptionText.setEditable(false);
        algoDescriptionText.setText(getDescription());
        JScrollPane algoScrollPane = new JScrollPane(algoDescriptionText);
        algoScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        algoScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        eastPanel.add(cellScrollPane);
        eastPanel.add(Box.createVerticalStrut(10)); // Add some space between components
        eastPanel.add(algoScrollPane);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runButton = new JButton("Run");
        runButton.setPreferredSize(new Dimension(80, 30));
        runButton.addActionListener(e -> {
            controller.run();
        });
        bottomPanel.add(runButton);

        stepButton = new JButton("Step");
        stepButton.setPreferredSize(new Dimension(80, 30));
        stepButton.addActionListener(e -> {
            controller.step();
        });
        bottomPanel.add(stepButton);

        rootPanel.add(eastPanel, BorderLayout.EAST);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void setController(T controller) {
        this.controller = controller;
        rootPanel.addKeyListener(controller);
    }

    @Override
    public void onControllerReady(T controller) {
        // NO OP
    }

    protected abstract String getDescription();

    public String getCellDescriptionText() {
        return cellDescriptionText.getText();
    }

    public void setCellDescriptionText(String cellDescriptionText) {
        this.cellDescriptionText.setText(cellDescriptionText);
    }

    public String getAlgoDescriptionText() {
        return algoDescriptionText.getText();
    }

    public void setAlgoDescriptionText(String algoDescriptionText) {
        this.algoDescriptionText.setText(algoDescriptionText);
    }
}
