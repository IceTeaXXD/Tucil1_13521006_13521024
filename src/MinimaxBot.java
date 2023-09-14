import javafx.scene.control.Button;

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
                    if (score >= bestScore) {
                        bestScore = score;
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
                System.out.println("Maximizing No move available");
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
                bestScore = Integer.MIN_VALUE;
                System.out.println("Minimizing No move available");
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
        if (i - 1 >= 0 && !board[i - 1][j].getText().equals(player)) {
            board[i - 1][j].setText(player);
        }
        if (i + 1 < 8 && !board[i + 1][j].getText().equals(player)) {
            board[i + 1][j].setText(player);
        }
        if (j - 1 >= 0 && !board[i][j - 1].getText().equals(player)) {
            board[i][j - 1].setText(player);
        }
        if (j + 1 < 8 && !board[i][j + 1].getText().equals(player)) {
            board[i][j + 1].setText(player);
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
}