package com.bngarren.algodemo;

import com.bngarren.algodemo.algos.BFS.BFSController;
import com.bngarren.algodemo.algos.BFS.BFSView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AlgoDemo extends JFrame {

    JPanel mainPanel;
    CardLayout mainLayout;
    private final Map<String, IAlgoView<?>> algoViews;
    AppKeyEventDispatcher keyEventDispatcher;
    private AbstractAlgoController<?> currentAlgoController;

    public AlgoDemo() {
        setTitle("AlgoDemo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Button.focus", new Color(0, 0, 0));
            setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupFrameDimensions();

        // Center the frame on the screen
        setLocationRelativeTo(null);

        mainLayout = new CardLayout();
        mainPanel = new JPanel(mainLayout);
        setLayout(new BorderLayout());

        add(mainPanel, BorderLayout.CENTER);

        algoViews = new HashMap<>();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Algorithms");
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Creates and sets a KeyEventDispatcher for the app. This class will handle intercepting global key events and passing them to a KeyListener, if active. The key listener is typically an IAlgoController, such that each concrete implementation can handle global key events as needed.
        initializeKeyEventDispatcher();

    }

    /**
     * Sets the frame size as a percentage of screen size
     */
    private void setupFrameDimensions() {
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the frame size as a percentage of the screen size
        int frameWidth = (int) (screenSize.width * 0.8); // 80% of screen width
        int frameHeight = (int) (screenSize.height * 0.8); // 80% of screen height

        // Set the frame size
        setSize(frameWidth, frameHeight);
    }

    private void initializeKeyEventDispatcher() {
        keyEventDispatcher = new AppKeyEventDispatcher();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    //uses bounded type parameters to ensure that the view and controller are compatible with each other
    private <V extends IAlgoView<C>, C extends IAlgoController<V>> void addAlgoView(Supplier<V> viewFactory, Supplier<C> controllerFactory) {
        V view = viewFactory.get();
        C controller = controllerFactory.get();
        controller.setView(view);
        view.setController(controller);
        controller.setup();
        view.onControllerReady(controller);

        algoViews.put(view.getTitle(), view);
        mainPanel.add(view.getRootPanel(), view.getTitle());

        JMenuItem menuItem = new JMenuItem(view.getTitle());
        menuItem.addActionListener(e -> {
            mainLayout.show(mainPanel, view.getTitle());
            keyEventDispatcher.setKeyListener(controller);
        });
        getJMenuBar().getMenu(0).add(menuItem);

        // Set the initial controller if it's the first view added
        if (currentAlgoController == null) {
            currentAlgoController = (AbstractAlgoController<?>) controller;
            keyEventDispatcher.setKeyListener(controller);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AlgoDemo ag = new AlgoDemo();

            // Add the algorithms/views/controllers
            ag.addAlgoView(BFSView::new, BFSController::new);

            ag.setVisible(true);
        });
    }
}
