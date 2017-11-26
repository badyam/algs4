/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       11/26/2017
 *  Last updated:  11/26/2017
 *
 *  Compilation:   javac SAP.java
 *  Execution:     java SAP
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Digraph;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private static class BFS {
        private static final int INFINITY = Integer.MAX_VALUE;
        private boolean[] marked;
        private int[] edgeTo;
        private int[] distTo;

        private BFS(Digraph G, int s) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertex(s);
            bfs(G, s);
        }

        private BFS(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            for (int v = 0; v < G.V(); v++)
                distTo[v] = INFINITY;
            validateVertices(sources);
            bfs(G, sources);
        }

        // BFS from single source
        private void bfs(Digraph G, int s) {
            Queue<Integer> q = new Queue<>();
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        // BFS from multiple sources
        private void bfs(Digraph G, Iterable<Integer> sources) {
            Queue<Integer> q = new Queue<>();
            for (int s : sources) {
                marked[s] = true;
                distTo[s] = 0;
                q.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        public Iterable<Integer> pathTo(int v) {
            validateVertex(v);

            Stack<Integer> path = new Stack<Integer>();
            int x;
            for (x = v; distTo[x] != 0; x = edgeTo[x])
                path.push(x);
            path.push(x);
            return path;
        }

        private Iterable<Integer> ancestors() {
            Stack<Integer> ancestors = new Stack<>();
            for (int i = 0; i < marked.length; i++)
                if (marked[i])
                    ancestors.push(i);
            return ancestors;
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertex(int v) {
            int ml = marked.length;
            if (v < 0 || v >= ml)
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (ml - 1));
        }

        // throw an IllegalArgumentException unless {@code 0 <= v < V}
        private void validateVertices(Iterable<Integer> vertices) {
            if (vertices == null) {
                throw new IllegalArgumentException("argument is null");
            }
            int ml = marked.length;
            for (int v : vertices) {
                if (v < 0 || v >= ml) {
                    throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (ml - 1));
                }
            }
        }
    }

    private final Digraph dg;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.dg = new Digraph(G);
    }

    private int length(BFS v, BFS w) {
        int minLength = -1;
        for (int a : v.ancestors()) {
            int wDistToA = w.distTo[a];
            if (wDistToA != BFS.INFINITY) {
                int length = v.distTo[a] + wDistToA;
                if (length < minLength || minLength == -1)
                    minLength = length;
            }
        }
        return minLength;
    }

    private int ancestor(BFS v, BFS w) {
        int minLength = -1;
        int ancestor = -1;
        for (int a : v.ancestors()) {
            int wDistToA = w.distTo[a];
            if (wDistToA != BFS.INFINITY) {
                int length = v.distTo[a] + wDistToA;
                if (length < minLength || minLength == -1) {
                    minLength = length;
                    ancestor = a;
                }
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(new BFS(dg, v), new BFS(dg, w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(new BFS(dg, v), new BFS(dg, w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return length(new BFS(dg, v), new BFS(dg, w));
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestor(new BFS(dg, v), new BFS(dg, w));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
