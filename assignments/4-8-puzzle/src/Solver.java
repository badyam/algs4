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
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final int moves;
    private final Queue<Board> solution = new Queue<>();
    private final boolean isSolvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> queue = new MinPQ<>(new NodeComparator());

        int move = 0;
        SearchNode node = new SearchNode(initial, move, null);
        queue.insert(node);

        // add alternative to detect unsolvable case
        // Board alternative = initial.twin();
        // queue.insert(alternative);

        boolean done = false;
        while (!done) {
            node = queue.delMin();
            solution.enqueue(node.board);
            move++;

            if (node.board.isGoal()) {
                done = true;
            } else {
                for (Board neighbor : node.board.neighbors()) {
                    queue.insert(new SearchNode(neighbor, move, node));
                }
            }
        }

        moves = move;
        isSolvable = true;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private static class SearchNode {
        private final SearchNode prevNode;
        private final Board board;
        private final int manhattan;
        private final int priority;

        private SearchNode(Board board, int move, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            manhattan = board.manhattan();
            priority = manhattan + move;
        }
    }

    private static class NodeComparator implements Comparator<SearchNode> {

        public int compare(SearchNode node1, SearchNode node2) {
            return Integer.compare(node1.priority, node2.priority);
        }

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
