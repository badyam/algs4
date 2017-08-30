/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/29/2017
 *  Last updated:  8/29/2017
 *
 *  Compilation:   javac Board.java
 *  Execution:     java Board
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int n;
    private final int[] blocks;

    // In generic case the goal board can be any combination of blocks.
    // In study case it's always ordered array with last 0
    // To save memory we won't be using another copy of Board as a goal board.
    // private final Board goal;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new int[n * n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int k = i * n + j;
                this.blocks[k] = blocks[i][j];
            }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int h = 0;
        for (int i = 0; i < blocks.length; i++)
            if (blocks[i] != 0 && blocks[i] != i + 1)
                h++;

        return h;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int m = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0 && blocks[i] != i + 1){
                int asIsPos = i;
                int toBePos = blocks[i] - 1;
                int y1 = asIsPos / n;
                int x1 = asIsPos % n;
                int y2 = toBePos / n;
                int x2 = toBePos % n;
                m += Math.abs(y2 - y1) + Math.abs(x2 - x1);
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < blocks.length - 1; i++)
            if (blocks[i] != i + 1)
                return false;

        return blocks[blocks.length - 1] == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) return false;

        for (int i = 0; i < that.blocks.length; i++)
            if (that.blocks[i] != blocks[i])
                return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            if (i > 0 && i % n == 0) sb.append("\n");
            sb.append(String.format("%5s", blocks[i] == 0 ? " " : Integer.toString(blocks[i])));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Board board;

        board = fixture("puzzle00");
        test("Dimension equals 100", board.dimension() == 10);
        test("It's a goal board", board.isGoal());
        test("hamming is 0", board.hamming() == 0);
        test("manhattan is 0", board.manhattan() == 0);

        board = fixture("puzzle4x4-01");
        test("Dimension equals 4", board.dimension() == 4);
        test("It isn't a goal board", !board.isGoal());
        test("hamming is 1", board.hamming() == 1);
        test("manhattan is 1", board.manhattan() == 1);

        board = fixture("puzzle3x3-31");
        test("Dimension equals 3", board.dimension() == 3);
        test("It isn't a goal board", !board.isGoal());
        test("hamming is 8", board.hamming() == 7);
        test("manhattan is 21", board.manhattan() == 21);
        StdOut.println(board.manhattan());
    }

    private static Board fixture(String inputFileName) {
        StdOut.println();
        StdOut.println(inputFileName);

        // create initial board from file
        In in = new In("in\\" + inputFileName + ".txt");
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();

        Board b = new Board(blocks);
        StdOut.println(b.toString());
        return b;
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }
}
