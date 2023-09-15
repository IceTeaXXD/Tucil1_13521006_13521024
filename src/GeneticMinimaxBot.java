import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.scene.control.Button;

public class GeneticMinimaxBot extends Bot {
    private static final int POPULATION_SIZE = 10;
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_GENERATIONS = 100;
    private static final int MAX_DEPTH = 3;

    private Random random = new Random();

    /**
     * This bot is a genetic algorithm bot. It will create a population of random
     * moves and evolve them using a genetic algorithm to find the best move.
     * 
     * @param board      The current state of the board.
     * @param roundsLeft The number of rounds left in the game.
     * 
     * @return The move that the bot will make.
     */
    public int[] move(Button[][] board, int roundsLeft) {
        // Create a copy of the board
        Button[][] copyBoard = new Button[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                copyBoard[x][y] = new Button();
                copyBoard[x][y].setText(board[x][y].getText());
            }
        }

        // Create a population of random moves
        ArrayList<int[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(getRandomMove(board));
        }

        // Evolve the population using a genetic algorithm
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            // Evaluate the fitness of each solution
            ArrayList<Integer> fitness = new ArrayList<>();
            for (int[] move : population) {
                fitness.add(evaluate(board, move[0], move[1]));
            }

            // Select the best solutions to be parents for the next generation
            ArrayList<int[]> parents = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE / 2; i++) {
                int index1 = rouletteWheelSelection(fitness);
                int index2 = rouletteWheelSelection(fitness);
                parents.add(population.get(index1));
                parents.add(population.get(index2));
            }

            // Generate new solutions by combining the genes of the parents
            ArrayList<int[]> offspring = new ArrayList<>();
            for (int i = 0; i < POPULATION_SIZE; i++) {
                int[] parent1 = parents.get(random.nextInt(parents.size()));
                int[] parent2 = parents.get(random.nextInt(parents.size()));
                int[] child = new int[2];
                child[0] = random.nextBoolean() ? parent1[0] : parent2[0];
                child[1] = random.nextBoolean() ? parent1[1] : parent2[1];
                offspring.add(child);
            }

            // Mutate some of the new solutions
            for (int[] move : offspring) {
                if (random.nextDouble() < MUTATION_RATE) {
                    move[0] = random.nextInt(8);
                    move[1] = random.nextInt(8);
                }
            }

            // Evaluate the fitness of the new solutions using Minimax
            ArrayList<Integer> minimaxFitness = new ArrayList<>();
            for (int[] move : offspring) {
                minimaxFitness.add(minimax(board, move[0], move[1], 0, roundsLeft, true));
            }

            // Select the best solution as the move to make
            int bestIndex = minimaxFitness.indexOf(Collections.max(minimaxFitness));
            int[] bestMove = offspring.get(bestIndex);
            if (copyBoard[bestMove[0]][bestMove[1]].getText().equals("")) {
                // System.out.println("Best Move: " + bestMove[0] + ", " + bestMove[1]);
                return bestMove;
            }

            // Replace the old population with the new offspring
            population = offspring;
        }

        // If no satisfactory solution is found, return a random move
        int[] move = getRandomMove(copyBoard);
        System.out.println("random");
        System.out.println(move[0] + ", " + move[1]);
        return move;
    }

    /**
     * This method selects an index from the fitness array using a roulette wheel
     * selection algorithm.
     * 
     * @param fitness The fitness array.
     * 
     * @return The selected index.
     */
    private int rouletteWheelSelection(ArrayList<Integer> fitness) {
        int totalFitness = 0;
        for (int f : fitness) {
            totalFitness += f;
        }
        double randomFitness = random.nextDouble() * totalFitness;
        int index = 0;
        while (randomFitness > 0) {
            randomFitness -= fitness.get(index);
            index++;
        }
        // check if index is 0, if so, return 0
        return index == 0 ? 0 : index - 1;
    }

    /**
     * This method generates a random move.
     * 
     * @param board The current state of the board.
     * 
     * @return The random move.
     */
    private int[] getRandomMove(Button[][] board) {
        int[] move = new int[2];
        do {
            move[0] = random.nextInt(8);
            move[1] = random.nextInt(8);
        } while (!board[move[0]][move[1]].getText().equals(""));
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

    /**
     * This method uses the Minimax algorithm to evaluate the fitness of a move.
     * 
     * @param board      The current state of the board.
     * @param row        The row of the move to evaluate.
     * @param col        The column of the move to evaluate.
     * @param roundsLeft The number of rounds left in the game.
     * @param maximizing Whether the current player is maximizing or minimizing.
     * 
     * @return The score of the move.
     */
    private int minimax(Button[][] board, int row, int col, int depth, int roundsLeft, boolean maximizing) {
        // Make the move
        board[row][col].setText("X");

        // Evaluate the score of the move
        int score = evaluate(board, row, col);

        // If the game is over or there are no more rounds left, return the score
        if (depth == MAX_DEPTH) {
            // System.out.println("score");
            board[row][col].setText("");
            return score;
        }

        // If the current player is maximizing, find the move with the highest score
        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board[r][c].getText().equals("")) {
                        int moveScore = minimax(board, r, c, depth + 1, roundsLeft - 1, false);
                        bestScore = Math.max(bestScore, moveScore);
                    }
                }
            }
            board[row][col].setText("");
            return bestScore;
        }
        // If the current player is minimizing, find the move with the lowest score
        else {
            int worstScore = Integer.MAX_VALUE;
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (board[r][c].getText().equals("")) {
                        int moveScore = minimax(board, r, c, depth + 1, roundsLeft - 1, true);
                        worstScore = Math.min(worstScore, moveScore);
                    }
                }
            }
            board[row][col].setText("");
            return worstScore;
        }
    }
}