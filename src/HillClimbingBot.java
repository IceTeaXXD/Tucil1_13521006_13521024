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
                    // Calculate the score of the current move
                    int score = evaluate(board, i, j);
                    if (score > bestScore) {
                        bestScore = score;
                        move = new int[] { i, j };
                    }
                }
            }
        }
        return move;
    }

    /**
     * This method evaluates the current state of the board and returns a score
     * based on the number of adjacent pieces.
     * 
     * @param board The current state of the board.
     * @param row   The row of the current move.
     * @param col   The column of the current move.
     * 
     * @return The score of the current state of the board.
     */
    public int evaluate(Button[][] board, int row, int col) {
        int score = 0;
        if (row - 1 >= 0 && board[row - 1][col].getText().equals("X")) {
            score++;
        }
        if (row + 1 < 8 && board[row + 1][col].getText().equals("X")) {
            score++;
        }
        if (col - 1 >= 0 && board[row][col - 1].getText().equals("X")) {
            score++;
        }
        if (col + 1 < 8 && board[row][col + 1].getText().equals("X")) {
            score++;
        }
        return score;
    }
}
