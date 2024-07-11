public class Solver {
    private int[][] board;
    private static final int SIZE = 9;
    private SolverCallback callback;

    public interface SolverCallback {
        void updateCell(int row, int col, int value);
        void delay(int milliseconds);
    }

    public Solver(int[][] board, SolverCallback callback) {
        this.board = board;
        this.callback = callback;
    }

    public boolean solve() {
        return solveHelper(0, 0);
    }

    private boolean solveHelper(int row, int col) {
        if (col == SIZE) {
            row++;
            col = 0;
        }
        if (row == SIZE) {
            return true;
        }
        if (board[row][col] != 0) {
            return solveHelper(row, col + 1);
        }
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                board[row][col] = num;
                callback.updateCell(row, col, num);
                callback.delay(50);
                if (solveHelper(row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
                callback.updateCell(row, col, 0);
                callback.delay(50);
            }
        }
        return false;
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num || 
                board[row - row % 3 + i / 3][col - col % 3 + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }
}
