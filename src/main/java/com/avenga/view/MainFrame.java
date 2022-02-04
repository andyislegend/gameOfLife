package com.avenga.view;

import com.avenga.game.Game;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private static final String TITLE = "Game of Life";
    private static final String START_BUTTON_ICON_CODE = "\u25B6";
    private static final String STOP_BUTTON_ICON_CODE = "\u25FC";

    private final JButton startButton;
    private final JButton stopButton;

    public MainFrame(Game game, JPanel gridPanel) {
        JFrame mainFrame = initMainFrame();
        Container content = mainFrame.getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        startButton = new JButton(START_BUTTON_ICON_CODE);
        startButton.addActionListener(e -> {
            game.start();
            startButton.setEnabled(false);
        });

        Container mediaPanel = new Container();
        mediaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        stopButton = new JButton(STOP_BUTTON_ICON_CODE);
        stopButton.addActionListener(e -> {
            game.terminate();
            startButton.setEnabled(false);
            stopButton.setEnabled(false);
        });

        mediaPanel.add(startButton);
        mediaPanel.add(stopButton);

        Container gridLayout = new Container();
        gridLayout.setLayout(new BoxLayout(gridLayout, BoxLayout.X_AXIS));
        gridLayout.add(gridPanel);

        content.add(mediaPanel);
        content.add(gridLayout);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(true);
        mainFrame.setVisible(true);
    }

    private static JFrame initMainFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(TITLE);
        return frame;
    }
}
