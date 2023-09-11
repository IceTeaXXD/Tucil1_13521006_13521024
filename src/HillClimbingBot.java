import javafx.scene.control.Button;

public class HillClimbingBot extends Bot {

    /**
     * This bot is a hill climbing bot. It will evaluate the current state of the
     * board and make a move that will maximize the score.
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
                    // Copy the board
                    Button[][] tempBoard = new Button[8][8];
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            tempBoard[x][y] = new Button();
                            tempBoard[x][y].setText(board[x][y].getText());
                        }
                    }
                    board[i][j].setText("O");
                    board = updateGameBoard(i, j, board, "O");
                    int score = evaluate(board);
                    if (score > bestScore) {
                        bestScore = score;
                        move = new int[] { i, j };
                    }
                    // Reset the board
                    board = tempBoard;
                }
            }
        }
        return move;
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
}
