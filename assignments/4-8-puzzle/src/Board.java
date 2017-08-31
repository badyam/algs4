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
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.sql.Array;
import java.util.Arrays;

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
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++) {
                int i = r * n + c;
                this.blocks[i] = blocks[r][c];
            }
    }

    // constructor which does not copy array
    private Board(int n, int[] blocks) {
        this.n = n;
        this.blocks = blocks;
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
        // for better performance let's take [r,c] coordinates
        // to have "*" operation, rather than "/" and "%"
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++) {
                int i = r * n + c;
                int toBe = blocks[i] - 1;
                // current block isn't empty (0)
                if (toBe != -1) {
                    int tr = toBe / n;
                    int tc = toBe % n;
                    m += Math.abs(tr - r) + Math.abs(tc - c);
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
        // sophisticated algorithm isn't required, just try swap two first blocks
        int first = 0;
        if (blocks[first] == 0) first++;

        // take block at right
        int second = first + 1;

        // it may be in the beginning of next row, then just take the block right under first
        if (second / n > 0) second = first + n;

        return twin(first, second);
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
        Stack<Board> neighbors = new Stack<>();
        // h-permutations
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n - 1; c++)
                neighbors.push(twin(r * n + c, r * n + c + 1));

        // v-permutations
        for (int c = 0; c < n; c++)
            for (int r = 0; r < n - 1; r++)
                neighbors.push(twin(r * n + c, (r + 1) * n + c));

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", n));
        for (int i = 0; i < blocks.length; i++) {
            if (i > 0 && i % n == 0) sb.append("\n");
            sb.append(String.format("%3d", blocks[i]));
        }

        return sb.toString();
    }

    private static void swap(int[] target, int from, int to) {
        int v = target[to];
        target[to] = target[from];
        target[from] = v;
    }

    // creates new board which is copy of current with on swap of given block (zero-block is OK)
    private Board twin(int i, int k) {
        int[] copy = Arrays.copyOf(blocks, blocks.length);
        swap(copy, i, k);
        return new Board(n, copy);
    }

    public static void main(String[] args) {
        Board board;

        board = fixture("puzzle00");
        test("Dimension equals 10", board.dimension() == 10);
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

        StdOut.println("-- neighbors --");
        int nc = 0;
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            nc++;
        }
        test("neighbors count is 2 * n * (n - 1)", nc == 2 * board.dimension() * (board.dimension() - 1));

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
        StdOut.println(b);
        return b;
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }
}
