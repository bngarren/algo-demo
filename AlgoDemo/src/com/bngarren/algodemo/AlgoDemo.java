package com.bngarren.algodemo;

import com.bngarren.algodemo.algos.BFS.BFSController;
import com.bngarren.algodemo.algos.BFS.BFSView;
import com.bngarren.algodemo.algos.Dijkstra.DijkstraController;
import com.bngarren.algodemo.algos.Dijkstra.DijkstraView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        setupMenuBar();

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

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Algorithms");
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void initializeKeyEventDispatcher() {
        keyEventDispatcher = new AppKeyEventDispatcher();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    //uses bounded type parameters to ensure that the view and controller are compatible with each other
    private <V extends IAlgoView<C>, C extends IAlgoController<V>> void addAlgoView(Supplier<C> controllerFactory) {

        C controller = controllerFactory.get();
        IAlgoView<C> view = controller.getView();
        view.setController(controller);
        controller.setup();

        algoViews.put(view.getTitle(), view);
        mainPanel.add(view.getRootPanel(), view.getTitle());

        JMenuItem menuItem = new JMenuItem(view.getTitle());
        menuItem.addActionListener(e -> {
            handleSelectMenuItem(e, view, controller);
        });
        getJMenuBar().getMenu(0).add(menuItem);

        // Set the initial controller if it's the first view added
        if (currentAlgoController == null) {
            currentAlgoController = (AbstractAlgoController<?>) controller;
            keyEventDispatcher.setKeyListener(controller);
        }
    }

    private <V extends IAlgoView<C>, C extends IAlgoController<V>> void handleSelectMenuItem(ActionEvent e, IAlgoView<C> view, C controller) {
        // Show the selected view
        mainLayout.show(mainPanel, view.getTitle());
        // Cancel the previous controller's algorithm
        if (currentAlgoController != null) {
            currentAlgoController.getAlgorithm().cancel();
        }

        currentAlgoController = (AbstractAlgoController<?>) controller;
        currentAlgoController.reset();
        // Selected controller takes over as global key listener
        keyEventDispatcher.setKeyListener(controller);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AlgoDemo ag = new AlgoDemo();

            // Add the algorithms/views/controllers
            ag.addAlgoView(() -> new BFSController(new BFSView()));

            ag.addAlgoView(() -> new DijkstraController(new DijkstraView()));

            ag.setVisible(true);
        });
    }
}
