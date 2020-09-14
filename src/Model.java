import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Model {

    private Buffer buffer;
    private int[][] field;
    private int score;
    private static int targetScore = 16;

    public static final int LEFT = 37;
    public static final int UP = 38;
    public static final int RIGHT = 39;
    public static final int DOWN = 40;
    public static final int UNDO = '0';

    private List<int[]> cellsAvailable;
    private boolean toStartNew;
    private Stack<int[][]> undo;

    public Model() {
        undo = new Stack<>();
        buffer = new Buffer();
        field = new int[4][4];
        findAvailableCells();
        score = 0;
        setRandom();
    }

    public Model startGame() {
        if (score > buffer.getScore()) {
            buffer.write(score);
        }
        if (toStartNew) {
            return new Model();
        }
        return this;
    }

    public int getField(int row, int col) {
        return field[row][col];
    }

    private void transpose() {
        for (int row = 0; row < field.length; row++) {
            for (int col = row; col < field[row].length; col++) {
                int t = field[row][col];
                field[row][col] = field[col][row];
                field[col][row] = t;
            }
        }
    }

    private void reverse() {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length / 2; col++) {
                int t = field[row][col];
                field[row][col] = field[row][field.length - 1 - col];
                field[row][field.length - 1 - col] = t;
            }
        }
    }

    private void merge() {
        for (int row = 0; row < field.length; row++) {
            int count = 0;
            for (int col = 1; col < field[row].length; col++) {
                if (field[row][col] != 0 && field[row][col] == field[row][count]) {
                    field[row][count] *= 2;
                    score += field[row][count];
                    field[row][col] = 0;
                    count++;
                } else if (field[row][col] != 0 || field[row][count] == 0) {
                    count++;
                }
            }
        }
    }

    private void shift() {
        boolean shiftAgain = false;
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length - 1; col++) {
                if (field[row][col] == 0 && field[row][col + 1] != 0) {
                    shiftAgain = true;
                }
                if (field[row][col] == 0 && field[row][col + 1] != 0) {
                    field[row][col] = field[row][col + 1];
                    field[row][col + 1] = 0;
                }
            }
        }
        if (shiftAgain) {
            shift();
        }
    }

    public void update(int dir) {
        int[][] toCheck = arrayCopy(field);
        toStartNew = true;
        switch (dir) {
            case LEFT:
                merge();
                shift();
                break;

            case UP:
                transpose();
                merge();
                shift();
                transpose();
                break;

            case RIGHT:
                reverse();
                merge();
                shift();
                reverse();
                break;

            case DOWN:
                transpose();
                reverse();
                merge();
                shift();
                reverse();
                transpose();
                break;

            case UNDO:
                undo();
        }
        if (!equals(toCheck, field)) {
            undo.push(toCheck);
            findAvailableCells();
            setRandom();
        }
    }

    public void undo() {
        field = undo.pop();
    }


    public boolean equals(int[][] first, int[][] second) {
        for (int i = 0; i < field.length; i++) {
            if (!Arrays.equals(first[i], second[i])) {
                return false;
            }
        }
        return true;
    }

    private void findAvailableCells() {
        cellsAvailable = new ArrayList<>();
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field.length; col++) {
                if (field[row][col] == 0) {
                    cellsAvailable.add(new int[]{row, col});
                }
            }
        }
    }

    private void setRandom() {
        if (!cellsAvailable.isEmpty()) {
            int[] randomPosition = cellsAvailable.get((int) (Math.random() * cellsAvailable.size()));
            field[randomPosition[0]][randomPosition[1]] = 2;
            cellsAvailable.remove(randomPosition);
        }
    }

    public boolean movesAvailable() {
        int[][] toCheck = arrayCopy(field);
        int score = this.score;
        boolean toStartNew = this.toStartNew;

        for (int i = LEFT; i <= DOWN; i++) {
            update(i);
        }
        boolean isAvailable = !equals(toCheck, field);
        this.score = score;
        this.toStartNew = toStartNew;
        field = toCheck;
        return isAvailable;
    }

    private int[][] arrayCopy(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field.length; col++) {
                if (field[row][col] == targetScore) {
                    return true;
                }
            }
        }
        return false;
    }

    public void increaseTargetScore() {
        if (targetScore < 2048) {
            targetScore *= 2;
        }
    }

    public void lowerTargetScore() {
        if (targetScore > 8) {
            targetScore /= 2;
        }
    }

    public int getBestScore() {
        return buffer.getScore();
    }

}
