/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package breakthrough;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author artur
 */
public class BreakThroughGUI {

    private final String O_TURN_LABEL = "O is playing";
    private final String X_TURN_LABEL = "X is playing";
    private final int MIN_SIZE = 6;
    private final int MID_SIZE = 8;
    private final int MAX_SIZE = 10;

    private final JFrame frame;
    private BreakThroughBoardGUI boardGUI;
    private final JPanel controlPanel;

    /**
     * Public constructor initializes the game.
     */
    public BreakThroughGUI() {
        frame = new JFrame("Break Through");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardGUI = new BreakThroughBoardGUI(MIN_SIZE);

        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.NORTH);
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu boardSize = new JMenu("Board size");
        menuBar.add(boardSize);

        boardSize.add(createSizeMenuItem("6x6", MIN_SIZE));
        boardSize.add(createSizeMenuItem("8x8", MID_SIZE));
        boardSize.add(createSizeMenuItem("10x10", MAX_SIZE));

        JMenuItem exitMenu = new JMenuItem("Exit");
        menuBar.add(exitMenu);
        exitMenu.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        controlPanel = new JPanel();
        frame.getContentPane().add(controlPanel);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel turnPanel = new JPanel();
        JLabel turnLabel = new JLabel();
        turnLabel.setText(O_TURN_LABEL);
        turnPanel.add(turnLabel);

        String directions[] = {"↑", "↓", "←", "→"};
        for (String direction : directions) {
            JButton button = new JButton(direction);
            button.setPreferredSize(new Dimension(45, 45));
            button.addActionListener(new DirectionListener(button.getText(), turnLabel));
            controlPanel.add(button);
        }

        controlPanel.add(turnPanel);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private class SizeOptionListener implements ActionListener {

        private final int size;

        /**
         * Public constructor initializes SizeOptionListener implementing ActionListener based on the given size.
         * @param size 
         */
        public SizeOptionListener(int size) {
            this.size = size;
        }

        /**
         * Override implements actionPerformed method which will create the new game based on the chosen size.
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.getContentPane().remove(boardGUI.getBoardPanel());
            boardGUI = new BreakThroughBoardGUI(size);
            frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.NORTH);
            frame.revalidate();
        }

    }

    private class DirectionListener implements ActionListener {

        private final String direction;
        private final JLabel turnLabel;

        /**
         * Public constructor initializes DirectionListener implementing ActionListener based on the given direction and turnLabel.
         * @param direction
         * @param turnLabel 
         */
        public DirectionListener(String direction, JLabel turnLabel) {
            this.direction = direction;
            this.turnLabel = turnLabel;
        }

        /**
         * Override implements actionPerformed method which will listen to the direction and perform the move; at the same time, it checks the state of the game and if the game is finished, it calls for the handleGameOver method.
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (boardGUI.getSelectedRow() == -1 || boardGUI.getSelectedColumn() == -1) {
                    JOptionPane.showMessageDialog(null, "Choose the doll first!", "Illegal behavior", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean successfulMove = boardGUI.getBoard().makeMove(direction, boardGUI.getSelectedRow(), boardGUI.getSelectedColumn());
                    if (successfulMove) {
                        boardGUI.getBoard().changeTurn();
                        boardGUI.getBoard().selectCell(boardGUI.getSelectedRow(), boardGUI.getSelectedColumn());
                        boardGUI.displayDolls();
                        boardGUI.displaySelect(boardGUI.getSelectedRow(), boardGUI.getSelectedColumn());
                        boardGUI.resetSelected();
                        if (boardGUI.getBoard().firstTurn()) {
                            turnLabel.setText(O_TURN_LABEL);
                        } else {
                            turnLabel.setText(X_TURN_LABEL);
                        }

                        Player currentResult = boardGUI.getBoard().getGameResult();
                        if (currentResult == Player.FIRST) {
                            handleGameOver("O player wins", turnLabel);
                        } else if (currentResult == Player.SECOND) {
                            handleGameOver("X player wins", turnLabel);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Choose wisely!", "Invalid Move", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (WrongDirectionException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Invalid Move", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Initializes the menu item for the menu size tab.
     * @param label
     * @param size
     * @return MenuItem for the specified size with the specified label.
     */
    private JMenuItem createSizeMenuItem(String label, int size) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.addActionListener(new SizeOptionListener(size));
        return menuItem;
    }

    /**
     * Handles game over state of the game that is reset all the fields and boards.
     * @param message
     * @param turnLabel 
     */
    private void handleGameOver(String message, JLabel turnLabel) {
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        frame.getContentPane().remove(boardGUI.getBoardPanel());
        boardGUI = new BreakThroughBoardGUI(MIN_SIZE);
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.NORTH);
        frame.revalidate();
        turnLabel.setText(O_TURN_LABEL);
    }
}
