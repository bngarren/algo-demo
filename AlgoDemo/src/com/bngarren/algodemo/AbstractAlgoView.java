package com.bngarren.algodemo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class AbstractAlgoView<T extends IAlgoController<?>> implements IAlgoView<T> {

    protected static final int EAST_PANEL_WIDTH = 400;
    protected static final int BUTTON_WIDTH = 120;
    protected static final int BUTTON_HEIGHT = 40;

    private JPanel rootPanel;
    protected JPanel eastPanel;
    protected JTextArea cellDescriptionText;
    protected JTextArea algoDescriptionText;
    protected JButton runButton;
    protected JButton stepButton;
    protected JButton resetButton;
    protected T controller;

    public AbstractAlgoView() {
        initializeRootPanel();
        initializeEastPanel();
        initializeBottomPanel();
    }

    private void initializeRootPanel() {
        rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void initializeEastPanel() {
        // Setup East panel with preferred size
        eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension(EAST_PANEL_WIDTH, 0));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        cellDescriptionText = new JTextArea();
        cellDescriptionText.setEditable(false);
        JScrollPane cellScrollPane = new JScrollPane(cellDescriptionText);
        cellScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        cellScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        algoDescriptionText = new JTextArea();
        algoDescriptionText.setEditable(false);
        setAlgoDescriptionText(getDescription());
        JScrollPane algoScrollPane = new JScrollPane(algoDescriptionText);
        algoScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        algoScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        eastPanel.add(cellScrollPane);
        eastPanel.add(Box.createVerticalStrut(10)); // Add some space between components
        eastPanel.add(algoScrollPane);

        rootPanel.add(eastPanel, BorderLayout.EAST);
    }

    private void initializeBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runButton = createButton("Run [SPACE]", e -> controller.run());
        stepButton = createButton("Step [.]", e -> controller.step());
        resetButton = createButton("Reset [R]", e -> controller.reset());

        bottomPanel.add(runButton);
        bottomPanel.add(stepButton);
        bottomPanel.add(resetButton);

        rootPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.addActionListener(actionListener);
        return button;
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

    /**
     * Sets the CENTER (BorderLayout) of the root panel to the given JPanel
     *
     * @param panel JPanel to set in the Center location
     */
    public void setCenterPanel(JPanel panel) {
        rootPanel.add(panel, BorderLayout.CENTER);
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
