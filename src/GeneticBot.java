import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.scene.control.Button;

public class GeneticBot extends Bot {
    private static final int POPULATION_SIZE = 100;
    private static final double MUTATION_RATE = 0.1;
    private static final int MAX_GENERATIONS = 2000;

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

            // Evaluate the fitness of the new solutions
            fitness.clear();
            for (int[] move : offspring) {
                fitness.add(evaluate(board, move[0], move[1]));
            }

            // Select the best solution as the move to make
            int bestIndex = fitness.indexOf(Collections.max(fitness));
            int[] bestMove = offspring.get(bestIndex);
            if (evaluate(board, bestMove[0], bestMove[1]) == 4) {
                // check if the best move already exists
                if (board[bestMove[0]][bestMove[1]].getText().equals("")) {
                    return bestMove;
                }
            }

            // Replace the old population with the new offspring
            population = offspring;
        }

        // If no satisfactory solution is found, return a random move
        return getRandomMove(board);
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
        return index - 1;
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
}