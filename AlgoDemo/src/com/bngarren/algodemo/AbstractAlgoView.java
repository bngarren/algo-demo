package com.bngarren.algodemo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class AbstractAlgoView<T extends IAlgoController<?>> implements IAlgoView<T> {

    protected JPanel rootPanel;
    protected JPanel eastPanel;
    protected JTextArea cellDescriptionTextArea;
    protected JTextArea algoDescriptionTextArea;
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

        cellDescriptionTextArea = new JTextArea();
        cellDescriptionTextArea.setEditable(false);
        JScrollPane cellScrollPane = new JScrollPane(cellDescriptionTextArea);
        cellScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        cellScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        algoDescriptionTextArea = new JTextArea();
        algoDescriptionTextArea.setEditable(false);
        algoDescriptionTextArea.setText(getDescription());
        JScrollPane algoScrollPane = new JScrollPane(algoDescriptionTextArea);
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

    protected abstract String getDescription();

}
