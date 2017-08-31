/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/29/2017
 *  Last updated:  8/29/2017
 *
 *  Compilation:   javac Solver.java
 *  Execution:     java Solver
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int moves = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<int> pq = new MinPQ<int>();

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return 0;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return null;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
