/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package breakthrough;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author artur
 */
public final class BreakThroughBoardGUI {

    private static final int BUTTON_SIZE = 45;
    
    private final JButton[][] guiBoard;
    private final Board logicBoard;
    private final JPanel boardPanel;
    private int selectedRow;
    private int selectedColumn;

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public Board getBoard() {
        return logicBoard;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    /**
     * Public constructor initializes the controller class to connect logic and GUI boards based on the given size.
     * @param size 
     */
    public BreakThroughBoardGUI(int size) {
        logicBoard = new Board(size);
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(size, size));
        guiBoard = new JButton[size][size];
        this.selectedRow = -1;
        this.selectedColumn = -1;
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                JButton button = new JButton();
                button.addActionListener(new CellListener(row, column));
                button.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                guiBoard[row][column] = button;
                boardPanel.add(button);
            }
        }
        displayDolls();
    }

    class CellListener implements ActionListener {

        private final int row;
        private final int column;

        /**
         * Public constructor initializes the CellListener implementing ActionListener based on the given row and column.
         * @param row
         * @param column 
         */
        public CellListener(int row, int column) {
            this.row = row;
            this.column = column;
        }

        /**
         * Override of the action performed to implement selecting of the cell.
         * @param e 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (logicBoard.firstTurn()) {
                manageAction(row, column, Player.FIRST, Player.SECOND);
            } else {
                manageAction(row, column, Player.SECOND, Player.FIRST);
            }
        }

        /**
         * Selects the cell based on the given row, column, player, and opponent.
         * @param row
         * @param column
         * @param player
         * @param opponent 
         */
        private void manageAction(int row, int column, Player player, Player opponent) {
            if (logicBoard.getCell(row, column).getOwner() == player) {
                if (logicBoard.noneSelected()) {
                    logicBoard.selectCell(row, column);
                    selectedRow = row;
                    selectedColumn = column;
                    displaySelect(selectedRow, selectedColumn);
                } else {
                    JOptionPane.showMessageDialog(null, "You have to play with the selected doll!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                }
            } else if (logicBoard.getCell(row, column).getOwner() == opponent) {
                JOptionPane.showMessageDialog(null, "That's not your doll!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "The empty cell cannot be selected!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Displays the dolls on the GUI board.
     */
    public void displayDolls() {
        for (int row = 0; row < logicBoard.getSize(); row++) {
            for (int column = 0; column < logicBoard.getSize(); column++) {
                if (logicBoard.getCell(row, column).getOwner() != Player.NONE) {
                    if (logicBoard.getCell(row, column).getOwner() == Player.FIRST) {
                        guiBoard[row][column].setText("O");
                    } else if (logicBoard.getCell(row, column).getOwner() == Player.SECOND) {
                        guiBoard[row][column].setText("X");
                    }
                } else {
                    guiBoard[row][column].setText("");
                }
            }
        }
    }

    /**
     * Displays the selected cell based on the given row and column.
     * @param row
     * @param column 
     */
    public void displaySelect(int row, int column) {
        if (logicBoard.getCell(row, column).isSelected()) {
            guiBoard[row][column].setBackground(Color.yellow);
        } else {
            guiBoard[row][column].setBackground(null);
        }
    }

    /**
     * Resets the selected row and column to -1.
     */
    public void resetSelected() {
        this.selectedRow = -1;
        this.selectedColumn = -1;
    }
}
