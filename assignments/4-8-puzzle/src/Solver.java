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
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private Stack<Board> solution = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        final NodeComparator comparator = new NodeComparator();
        MinPQ<SearchNode> queue = new MinPQ<>(comparator);
        MinPQ<SearchNode> twinQueue = new MinPQ<>(comparator);

        SearchNode node = new SearchNode(initial, null);
        queue.insert(node);

        twinQueue.insert(new SearchNode(initial.twin(), null));

        boolean win = false;
        boolean twinWin = false;
        while (!win && !twinWin) {
            node = queue.delMin();
            SearchNode twinNode = twinQueue.delMin();

            win = node.board.isGoal();
            if (!win) {
                twinWin = twinNode.board.isGoal();

                // none wins
                if (!twinWin) {
                    moveNext(queue, node);
                    moveNext(twinQueue, twinNode);
                }
            }
        }

        if (win) {
            solution = new Stack<>();
            while (node != null) {
                solution.push(node.board);
                node = node.prevNode;
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution != null ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private void moveNext(MinPQ<SearchNode> queue, SearchNode node) {
        for (Board neighbor : node.board.neighbors()) {
            // critical optimization
            if (node.prevNode == null || !node.prevNode.board.equals(neighbor))
                queue.insert(new SearchNode(neighbor, node));
        }
    }

    private static class SearchNode {
        private final SearchNode prevNode;
        private final Board board;
        private final int manhattan;
        private final int priority;
        private final int move;

        private SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            move = prevNode == null ? 0 : prevNode.move + 1;
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
