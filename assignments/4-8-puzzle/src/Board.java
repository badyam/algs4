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
import java.util.Arrays;

public class Board {

    private final int n;
    private final int[] blocks;

    // index of blank position
    private final int blank;

    // In generic case the goal board can be any combination of blocks.
    // In study case it's always ordered array with last 0
    // To save memory we won't be using another copy of Board as a goal board.
    // private final Board goal;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        this.blocks = new int[n * n];
        int b = -1;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++) {
                int i = r * n + c;
                int v = blocks[r][c];
                this.blocks[i] = v;
                if (v == 0)
                    b = i;
            }

        blank = b;
    }

    // constructor which does not copy array
    private Board(int n, int[] blocks, int blank) {
        this.n = n;
        this.blocks = blocks;
        this.blank = blank;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        int h = 0;
        for (int i = 0; i < blocks.length; i++)
            if (i != blank && blocks[i] != i + 1)
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
                if (i == blank)
                    continue;
                int toBe = blocks[i] - 1;
                int tr = toBe / n;
                int tc = toBe % n;
                m += Math.abs(tr - r) + Math.abs(tc - c);
            }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        // quick check
        if (blank != blocks.length - 1)
            return false;

        // full check
        for (int i = 0; i < blocks.length - 1; i++)
            if (blocks[i] != i + 1)
                return false;

        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // sophisticated algorithm isn't required, just try swap two first blocks
        return blank == 0
                ? twin(1, 2)
                : blank == 1
                    ? twin(0, 2)
                    : twin(0, 1);
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
    // that boards, that can be obtained by swapping one block with blank
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();

        int r = blank / n;
        int c = blank % n;

        // v-permutations
        if (r > 0)
            neighbors.push(twin((r - 1) * n + c, blank));
        if (r < n - 1)
            neighbors.push(twin((r + 1) * n + c, blank));

        // h-permutations
        if (c > 0)
            neighbors.push(twin(r * n + c - 1, blank));
        if (c < n - 1)
            neighbors.push(twin(r * n + c + 1, blank));

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d%n", n));
        for (int i = 0; i < blocks.length; i++) {
            if (i > 0 && i % n == 0) sb.append(String.format("%n"));
            sb.append(String.format("%2d ", blocks[i]));
        }

        return sb.toString();
    }

    private static void swap(int[] target, int from, int to) {
        int v = target[to];
        target[to] = target[from];
        target[from] = v;
    }

    // creates new board which is copy of current with two blocks swapped (zero-block is OK)
    private Board twin(int i, int k) {
        int[] copy = Arrays.copyOf(blocks, blocks.length);
        swap(copy, i, k);
        int newBlank = i == blank ? k : k == blank ? i : blank;
        return new Board(n, copy, newBlank);
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

        board = fixture("puzzle2x2-unsolvable2");
        test("Dimension equals 2", board.dimension() == 2);
        test("It isn't a goal board", !board.isGoal());
        test("hamming is 3", board.hamming() == 3);
        test("manhattan is 4", board.manhattan() == 4);
        int nc = count(board.neighbors());
        test("neighbors count is 2", nc == 2);

        Board twin = board.twin();
        test("twin is {0, 2, 1, 3}",
                twin.blocks[0] == 0 &&
                twin.blocks[1] == 2 &&
                twin.blocks[2] == 1 &&
                twin.blocks[3] == 3);

        StdOut.println("-- neighbors --");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
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

    private static int count(Iterable<Board> boards) {
        int c = 0;
        for (Board neighbor : boards) {
            c++;
        }
        return c;
    }
}
