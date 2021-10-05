import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int size;
    private final int[][] layout;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles[0] == null)
            throw new IllegalArgumentException("null entries in given points.");
        size = tiles[0].length;
        if (size < 2 || size >= 128)
            throw new IllegalArgumentException("size should between 2 and 138.");
        layout = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                layout[i][j] = tiles[i][j];
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(size + "\n");
        sb.append(toString2D(layout));
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int num = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (layout[i][j] != 1 + i * size + j)
                    num++;
            }
        return num - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int num = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (layout[i][j] > 0)
                    num += Math.abs((layout[i][j] - 1) / size - i) + Math.abs((layout[i][j] - 1) % size - j);
            }
        return num;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board dst = (Board) y;
        if (!Arrays.deepEquals(this.layout, dst.layout))
            return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<Board>();
        int[] zero = findEmpty();
        if (zero.length == 0)
            return null;
        int x0 = zero[0];
        int y0 = zero[1];

        if (x0 > 0) {
            int[][] blocksCopy = copyMDArray(layout);
            swap(blocksCopy, x0, y0, x0 - 1, y0);
            queue.enqueue(new Board(blocksCopy));
        }

        if (x0 < size - 1) {
            int[][] blocksCopy = copyMDArray(layout);
            swap(blocksCopy, x0, y0, x0 + 1, y0);
            queue.enqueue(new Board(blocksCopy));
        }

        if (y0 > 0) {
            int[][] blocksCopy = copyMDArray(layout);
            swap(blocksCopy, x0, y0, x0, y0 - 1);
            queue.enqueue(new Board(blocksCopy));
        }

        if (y0 < size - 1) {
            int[][] blocksCopy = copyMDArray(layout);
            swap(blocksCopy, x0, y0, x0, y0 + 1);
            queue.enqueue(new Board(blocksCopy));
        }
        return queue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] blocksCopy = copyMDArray(layout);
        int value;
        int lastValue = blocksCopy[0][0];
        for (int row = 0; row < blocksCopy.length; row++) {
            for (int col = 0; col < blocksCopy.length; col++) {
                value = blocksCopy[row][col];
                if (value != 0 && lastValue != 0 && col > 0) {
                    blocksCopy[row][col] = lastValue;
                    blocksCopy[row][col - 1] = value;
                    return new Board(blocksCopy);
                }
                lastValue = value;
            }
        }
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println("Dim: " + initial.dimension());
        StdOut.println("Hamming: " + initial.hamming());
        StdOut.println("Mahattan: " + initial.manhattan());
        StdOut.println("Is it goal? " + initial.isGoal());
        StdOut.println("twin: " + initial.twin());
        for (Board b : initial.neighbors())
            StdOut.println("Neighbor " + b);
    }

    private static int[][] copyMDArray(int[][] input) {
        int[][] output = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Arrays.copyOf(input[i], input[i].length);
        }
        return output;
    }

    private static void swap(int[][] input, int row, int col, int toRow, int toCol) {
        int first = input[row][col];
        input[row][col] = input[toRow][toCol];
        input[toRow][toCol] = first;
    }

    // need this util because System.arrayCopy and Arrays.copyOf
    // will get this wrong and use same pointers
    // (a 2D array is a 1D array of 1D arrays, etc)
    private static String toString2D(int[][] input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input.length; j++) {
                int digit = input[i][j];
                sb.append(" " + digit + " ");
            }
            if (i < input.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private int[] findEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (layout[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }
}
