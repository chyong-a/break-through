/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package breakthrough;

/**
 *
 * @author artur
 */
public class Board {

    public static final String UP = "↑";
    public static final String DOWN = "↓";
    public static final String LEFT = "←";
    public static final String RIGHT = "→";

    private final Cell[][] board;
    private final int size;
    private boolean firstTurn;

    /**
     * Public constructor initializes the logic board of the game based on the given size.
     * @param size 
     */
    public Board(int size) {
        this.size = size;
        board = new Cell[size][size];
        firstTurn = true;

        for (int column = 0; column < size; column++) {
            board[0][column] = new Cell(Player.FIRST);
            board[1][column] = new Cell(Player.FIRST);

            board[size - 2][column] = new Cell(Player.SECOND);
            board[size - 1][column] = new Cell(Player.SECOND);
        }

        for (int row = 2; row < size - 2; row++) {
            for (int column = 0; column < size; column++) {
                board[row][column] = new Cell(Player.NONE);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int column) {
        return board[row][column];
    }

    /**
     * Returns Boolean value to define whose turn it is.
     * @return true if it's the turn of the first player, otherwise, false.
     */
    public boolean firstTurn() {
        return firstTurn;
    }

    /**
     * Carries the whole logic of a move.
     * @param direction
     * @param currentRow
     * @param currentColumn
     * @return true if a move was successful, otherwise, false.
     * @throws WrongDirectionException 
     */
    public boolean makeMove(String direction, int currentRow, int currentColumn) throws WrongDirectionException {
        boolean successfulMove = false;
        if (direction.equals(UP)) {
            if (!firstTurn) {
                if (isNewCellFree(currentRow - 1, currentColumn)) {
                    performMove(currentRow, currentColumn, currentRow - 1, currentColumn, Player.SECOND);
                    successfulMove = true;
                } else if (isNewCellOccupiedByPlayer(currentRow - 1, currentColumn, Player.FIRST)) {
                    throw new WrongDirectionException("You cannot move forward onto your opponent's doll! Only forward diagonally!");
                } else {
                    throw new WrongDirectionException("You cannot go onto your dolls!");
                }
            } else {
                throw new WrongDirectionException("You can only move towards your opponent!");
            }
        } else if (direction.equals(DOWN)) {
            if (firstTurn) {
                if (isNewCellFree(currentRow + 1, currentColumn)) {
                    performMove(currentRow, currentColumn, currentRow + 1, currentColumn, Player.FIRST);
                    successfulMove = true;
                } else if (isNewCellOccupiedByPlayer(currentRow + 1, currentColumn, Player.SECOND)) {
                    throw new WrongDirectionException("You cannot move forward onto your opponent's doll! Only forward diagonally!");
                } else {
                    throw new WrongDirectionException("You cannot go onto your dolls!");
                }
            } else {
                throw new WrongDirectionException("You can only move towards your opponent!");
            }
        } else if (direction.equals(LEFT)) {
            if (firstTurn) {
                if (currentColumn != 0) {
                    if (isValidDiagonalMove(currentRow + 1, currentColumn - 1, Player.SECOND)) {
                        performMove(currentRow, currentColumn, currentRow + 1, currentColumn - 1, Player.FIRST);
                        successfulMove = true;
                    } else {
                        throw new WrongDirectionException("You cannot go onto your dolls!");
                    }
                } else {
                    throw new WrongDirectionException("There is a wall on the left!");
                }
            } else {
                if (currentColumn != 0) {
                    if (isValidDiagonalMove(currentRow - 1, currentColumn - 1, Player.FIRST)) {
                        performMove(currentRow, currentColumn, currentRow - 1, currentColumn - 1, Player.SECOND);
                        successfulMove = true;
                    } else {
                        throw new WrongDirectionException("You cannot go onto your dolls!");
                    }
                } else {
                    throw new WrongDirectionException("There is a wall on the left!");
                }
            }
        } else if (direction.equals(RIGHT)) {
            if (firstTurn) {
                if (currentColumn + 1 != size) {
                    if (isValidDiagonalMove(currentRow + 1, currentColumn + 1, Player.SECOND)) {
                        performMove(currentRow, currentColumn, currentRow + 1, currentColumn + 1, Player.FIRST);
                        successfulMove = true;
                    } else {
                        throw new WrongDirectionException("You cannot go onto your doll!");
                    }
                } else {
                    throw new WrongDirectionException("There is a wall on the right!");
                }
            } else {
                if (currentColumn + 1 != size) {
                    if (isValidDiagonalMove(currentRow - 1, currentColumn + 1, Player.FIRST)) {
                        performMove(currentRow, currentColumn, currentRow - 1, currentColumn + 1, Player.SECOND);
                        successfulMove = true;
                    } else {
                        throw new WrongDirectionException("You cannot go onto your doll!");
                    }
                } else {
                    throw new WrongDirectionException("There is a wall on the right!");
                }
            }
        }
        return successfulMove;
    }

    /**
     * Switches the turns.
     */
    public void changeTurn() {
        this.firstTurn = !firstTurn;
    }

    /**
     * Selects and deselects the specified cell in the board.
     * @param row
     * @param column 
     */
    public void selectCell(int row, int column) {
        board[row][column].select();
    }

    /**
     * Checks whether there are any cells selected.
     * @return true if one of the cells has already been selected, otherwise, false.
     */
    public boolean noneSelected() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (board[row][column].isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Gets the current state of the game; the player who reaches the opponent's side wins.
     * @return SECOND if the second player wins, FIRST if the first player wins, NONE if the game is still in progress.
     */
    public Player getGameResult() {
        for (int column = 0; column < size; column++) {
            if (isTopCellOwnedBySecondPlayer(column)) {
                return Player.SECOND;
            }
            if (isBottomCellOwnedByFirstPlayer(column)) {
                return Player.FIRST;
            }
        }
        return Player.NONE;
    }

    /**
     * Checks whether the diagonal move is valid.
     * @param newRow
     * @param newColumn
     * @param opponent
     * @return true if the move is valid, otherwise, false.
     */
    private boolean isValidDiagonalMove(int newRow, int newColumn, Player opponent) {
        return isNewCellFree(newRow, newColumn) || isNewCellOccupiedByPlayer(newRow, newColumn, opponent);
    }

    /**
     * Checks whether the given cell is free.
     * @param newRow
     * @param newColumn
     * @return true if the cell is free, otherwise, false.
     */
    private boolean isNewCellFree(int newRow, int newColumn) {
        return board[newRow][newColumn].getOwner() == Player.NONE;
    }

    /**
     * Checks whether the given cell is occupied by the given player.
     * @param newRow
     * @param newColumn
     * @param player
     * @return true if the cell is occupied, otherwise, false.
     */
    private boolean isNewCellOccupiedByPlayer(int newRow, int newColumn, Player player) {
        return board[newRow][newColumn].getOwner() == player;
    }

    /**
     * Releases the previous cell and occupies the new cell by the new player.
     * @param currentRow
     * @param currentColumn
     * @param newRow
     * @param newColumn
     * @param player 
     */
    private void performMove(int currentRow, int currentColumn, int newRow, int newColumn, Player player) {
        board[currentRow][currentColumn].occupyByNone();
        board[newRow][newColumn].occupyByPlayer(player);
    }

    /**
     * Checks if the SECOND player has reached the top cell.
     * @param column
     * @return true if the SECOND player has reached the top cell, otherwise, false.
     */
    private boolean isTopCellOwnedBySecondPlayer(int column) {
        return board[0][column].getOwner() == Player.SECOND;
    }

    /**
     * Checks if the FIRST player has reached the bottom cell.
     * @param column
     * @return true if the FRST player has reacher the bottom cell, otherwise, false.
     */
    private boolean isBottomCellOwnedByFirstPlayer(int column) {
        return board[size - 1][column].getOwner() == Player.FIRST;
    }
}
