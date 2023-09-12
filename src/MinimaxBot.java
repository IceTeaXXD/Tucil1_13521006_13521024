import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;

public class MinimaxBot extends Bot {

    /**
     * This bot is a minimax bot. It will evaluate the current state of the board
     * and make a move that will maximize the score.
     * 
     * @param board      The current state of the board.
     * @param roundsLeft The number of rounds left in the game.
     * 
     * @return The move that the bot will make.
     */
    public int[] move(Button[][] board, int roundsLeft) {
        int[] move = new int[2];
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Check if the board is available
                if (board[i][j].getText().equals("")) {
                    Button[][] copyBoard = new Button[8][8];
                    // Copy the board
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            copyBoard[x][y] = new Button();
                            copyBoard[x][y].setText(board[x][y].getText());
                        }
                    }
                    // Make a move
                    copyBoard[i][j].setText("O");
                    copyBoard = updateGameBoard(i, j, copyBoard, "O");
                    // Evaluate the board
                    int score = minimax(copyBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false, roundsLeft);
                    // Update the best score
                    if (score > bestScore) {
                        bestScore = score;
                        System.out.println("Best score: " + bestScore);
                        // printBoard(copyBoard);
                        // System.out.println();
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    public int minimax(Button[][] board, int depth, int alpha, int beta, boolean isMaximizing, int roundsLeft) {
        // If the game is over or the depth is reached, evaluate the board
        if (roundsLeft == 0 || depth == 3) {
            return evaluate(board);
        }
        boolean prune = false;
        // If it is the maximizing player's turn
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            // Check all possible moves
            for (int i = 0; i < 8; i++) {
                if (prune) {
                    break;
                }
                for (int j = 0; j < 8; j++) {
                    // Check if the board is available
                    if (board[i][j].getText().equals("")) {
                        Button[][] copyBoard = new Button[8][8];
                        // Copy the board
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                copyBoard[x][y] = new Button();
                                copyBoard[x][y].setText(board[x][y].getText());
                            }
                        }
                        // Make a move
                        copyBoard[i][j].setText("O");
                        copyBoard = updateGameBoard(i, j, copyBoard, "O");
                        // Evaluate the board
                        int score = minimax(copyBoard, depth + 1, alpha, beta, false, roundsLeft - 1);
                        // Update the best score
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, score);
                        // Prune the tree
                        if (beta <= alpha) {
                            prune = true;
                            break;
                        }
                    }
                }
            }
            if (bestScore == Integer.MIN_VALUE) {
                // return evaluate(board);
                System.out.println("No move available");
            }
            return bestScore;
        }

        // If it is the minimizing player's turn
        else {
            int bestScore = Integer.MAX_VALUE;
            // Check all possible moves
            for (int i = 0; i < 8; i++) {
                if (prune) {
                    break;
                }
                for (int j = 0; j < 8; j++) {
                    // Check if the board is available
                    if (board[i][j].getText().equals("")) {
                        Button[][] copyBoard = new Button[8][8];
                        // Copy the board
                        for (int x = 0; x < 8; x++) {
                            for (int y = 0; y < 8; y++) {
                                copyBoard[x][y] = new Button();
                                copyBoard[x][y].setText(board[x][y].getText());
                            }
                        }
                        // Make a move
                        copyBoard[i][j].setText("X");
                        copyBoard = updateGameBoard(i, j, copyBoard, "X");
                        // Evaluate the board
                        int score = minimax(copyBoard, depth + 1, alpha, beta, true, roundsLeft - 1);
                        // Update the best score
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, score);
                        // Prune the tree
                        if (beta <= alpha) {
                            prune = true;
                            break;
                        }
                    }
                }
            }
            if (bestScore == Integer.MAX_VALUE) {
                // return evaluate(board);
                System.out.println("No move available");
            }
            return bestScore;
        }
    }

    /**
     * Get the score of the player
     * 
     * @param board is the game board
     * @param i     The row number of the button clicked.
     * @param j     The column number of the button clicked.
     * 
     * @return the score of the player
     *         [0] is the score of the player (X)
     *         [1] is the score of the bot (O)
     */
    private Button[][] updateGameBoard(int i, int j, Button[][] board, String player) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries: First & last row and first & last column.
        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0) // If clicked button in first row, no preceding row exists.
            startRow = i;
        else // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= 8) // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0) // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= 8) // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;

        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int x = startRow; x <= endRow; x++) {
            for (int y = startColumn; y <= endColumn; y++) {
                // if x and y is diagonal to i and j, skip
                if (i - 1 == x && j - 1 == y) {
                    continue;
                } else if (i - 1 == x && j + 1 == y) {
                    continue;
                } else if (i + 1 == x && j - 1 == y) {
                    continue;
                } else if (i + 1 == x && j + 1 == y) {
                    continue;
                } else {
                    if (board[x][y].getText().equals("X") && player.equals("O")) {
                        board[x][y].setText("O");
                    } else if (board[x][y].getText().equals("O") && player.equals("X")) {
                        board[x][y].setText("X");
                    }
                }
            }
        }
        return board;
    }

    /**
     * This method evaluates the current state of the board and returns a score
     * based on the number of X's and O's on the board.
     * 
     * @param board The current state of the board.
     * 
     * @return The score of the current state of the board.
     */
    public int evaluate(Button[][] board) {
        int playerXScore = 0;
        int playerOScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getText().equals("X")) {
                    playerXScore++;
                } else if (board[i][j].getText().equals("O")) {
                    playerOScore++;
                }
            }
        }
        return playerOScore - playerXScore;
    }

    public void printBoard(Button[][] board) {
        // it must be 8x8 grid, pay attention to the whitespaces
        for (int i = 0; i < 8; i++) {
            System.out.print("|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getText().equals(""))
                    System.out.print(" |");
                else
                    System.out.print(board[i][j].getText() + "|");
            }
            System.out.println();
        }
    }

    public int boardElements(Button[][] board) {
        int elements = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board[i][j].getText().equals(""))
                    elements++;
            }
        }
        return elements;
    }
}