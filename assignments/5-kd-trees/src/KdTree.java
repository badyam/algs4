/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       9/6/2017
 *  Last updated:  9/6/2017
 *
 *  Compilation:   javac KdTree.java
 *  Execution:     java KdTree
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.*;

public class KdTree {

    private static class Node {
        private final Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node left;        // the left/bottom subtree
        private Node right;        // the right/top subtree

        public Node(Point2D point) {

            this.p = point;
        }

    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            root = new Node(p);
            size = 1;
            return;
        }

        if (contains(p)) return;

        insert(root, true, p);
        size++;
    }

    private void insert(Node intoNode, boolean isV, Point2D point) {
        int cmp = isV
                ? Double.compare(point.x(), intoNode.p.x())
                : Double.compare(point.y(), intoNode.p.y());

        if (cmp <= 0) { // left
            if (intoNode.left == null)
                intoNode.left = new Node(point);
            else
                insert(intoNode.left, !isV, point);
        } else { // right
            if (intoNode.right == null)
                intoNode.right = new Node(point);
            else
                insert(intoNode.right, !isV, point);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return find(root, true, p) != null;
    }

    /**
     * @param node  - node to search in (inclusive)
     * @param isV   - if true - vertical divider (left and right); if false - horizontal (top and bottom)
     * @param point - point to find
     * @return node
     */
    private Node find(Node node, boolean isV, Point2D point) {
        if (node == null) return null;
        if (node.p.equals(point)) return node;
        int cmp = isV
                ? Double.compare(point.x(), node.p.x())
                : Double.compare(point.y(), node.p.y());
        if (cmp <= 0) return find(node.left, !isV, point);
        else return find(node.right, !isV, point);
    }


    // draw all points to standard draw
    public void draw() {
        drawNode(root, null, true, true);
    }

    public void drawNode(Node node, Node parent, boolean isV, boolean isLeft) {
        if (node == null) return;

        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        // draw lines
        StdDraw.setPenRadius();
        if (isV) {
            StdDraw.setPenColor(StdDraw.RED);
            if (isLeft)
                StdDraw.line(node.p.x(), 0, node.p.x(), parent != null ? parent.p.y() : 1);
            else
                StdDraw.line(node.p.x(), parent != null ? parent.p.y() : 0, node.p.x(), 1);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            if (isLeft)
                StdDraw.line(0, node.p.y(), parent != null ? parent.p.x() : 1, node.p.y());
            else
                StdDraw.line(parent != null ? parent.p.x() : 0, node.p.y(), 1, node.p.y());
        }

        drawNode(node.left, node, !isV, true);
        drawNode(node.right, node, !isV, false);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        final Queue<Point2D> result = new Queue<>();

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D target) {
        if (target == null) throw new IllegalArgumentException();

        Point2D np = null;
        double minDistance = Double.MAX_VALUE;
        return np;
    }

    public static void main(String[] args) {
        KdTree pointSET;
        int v;

        pointSET = fixture("Construction");
        test("is empty", pointSET.isEmpty());
        test("size is zero", pointSET.size() == 0);

        pointSET = fixture("insert two points");
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.2, 0.2));
        test("is not empty", !pointSET.isEmpty());
        test("size is 2", pointSET.size() == 2);

    }

    private static KdTree fixture(String description) {
        StdOut.println();
        StdOut.println(description);
        return new KdTree();
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }

}
