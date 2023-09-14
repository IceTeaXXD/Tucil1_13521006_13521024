import javafx.scene.control.Button;

public class RandomBot extends Bot {
    public int[] move(Button[][] board, int roundsLeft) {
        int[] move = new int[2];
        int randomRow = (int) (Math.random() * 8);
        int randomColumn = (int) (Math.random() * 8);
        while (!board[randomRow][randomColumn].getText().equals("")) {
            randomRow = (int) (Math.random() * 8);
            randomColumn = (int) (Math.random() * 8);
        }
        move[0] = randomRow;
        move[1] = randomColumn;
        return move;
    }
}
