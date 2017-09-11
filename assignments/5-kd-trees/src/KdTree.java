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

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static class Node {
        // the point
        private final Point2D point;

        // the left/bottom subtree
        private Node left;

        // the right/top subtree
        private Node right;

        // the axis-aligned rectangle corresponding to the node
        private final RectHV area;

        private Node(Point2D point, RectHV area) {

            this.point = point;
            this.area = area;
        }
    }

    private static class NearestChampion {
        private double distance;
        private Point2D point;
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
            root = new Node(p, new RectHV(0, 0, 1, 1));
            size = 1;
            return;
        }

        if (contains(p)) return;

        insert(root, true, p);
        size++;
    }

    /**
     * Inserts new node with point into parent not
     *
     * @param parent
     * @param isV    - is parent node vertically orientated
     * @param point
     */
    private void insert(Node parent, boolean isV, Point2D point) {
        int cmp = isV
                ? Double.compare(point.x(), parent.point.x())
                : Double.compare(point.y(), parent.point.y());

        // ! reference algorithm treats '=' as left, see circle10.png
        if (cmp >= 0) { // left
            if (parent.left == null) {
                RectHV area = isV
                        ? new RectHV(parent.point.x(), parent.area.ymin(), parent.area.xmax(), parent.area.ymax())
                        : new RectHV(parent.area.xmin(), parent.point.y(), parent.area.xmax(), parent.area.ymax());
                parent.left = new Node(point, area);
            } else
                insert(parent.left, !isV, point);
        } else { // right
            if (parent.right == null) {
                RectHV area = isV
                        ? new RectHV(parent.area.xmin(), parent.area.ymin(), parent.point.x(), parent.area.ymax())
                        : new RectHV(parent.area.xmin(), parent.area.ymin(), parent.area.xmax(), parent.point.y());
                parent.right = new Node(point, area);
            } else
                insert(parent.right, !isV, point);
        }
    }

    // does the set contain point?
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
        if (node.point.equals(point)) return node;
        int cmp = isV
                ? Double.compare(point.x(), node.point.x())
                : Double.compare(point.y(), node.point.y());

        return find(cmp >= 0 ? node.left : node.right, !isV, point);
    }


    // draw all points to standard draw
    public void draw() {
        drawNode(root, true);
    }

    private void drawNode(Node node, boolean isV) {
        if (node == null) return;

        // draw lines
        StdDraw.setPenRadius();
        if (isV) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.area.ymin(), node.point.x(), node.area.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.area.xmin(), node.point.y(), node.area.xmax(), node.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        drawNode(node.right, !isV);
        drawNode(node.left, !isV);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        final Queue<Point2D> result = new Queue<>();
        if (size == 0) return result;

        range(root, rect, true, result);

        return result;
    }

    private void range(Node node, RectHV rect, boolean isV, Queue<Point2D> result) {
        if (!rect.intersects(node.area))
            return;

        if (rect.contains(node.point))
            result.enqueue(node.point);

        if (node.right != null)
            range(node.right, rect, !isV, result);
        if (node.left != null)
            range(node.left, rect, !isV, result);
    }

    // a nearest neighbor in the set to point; null if the set is empty
    public Point2D nearest(Point2D target) {
        if (target == null) throw new IllegalArgumentException();
        if (size == 0) return null;

        NearestChampion champion = new NearestChampion();
        champion.distance = Double.MAX_VALUE;
        champion.point = null;

        findNearest(target, root, true, champion);

        return champion.point;
    }

    private void findNearest(Point2D target, Node node, boolean isV, NearestChampion champion) {

        double distance = target.distanceSquaredTo(node.point);
        if (distance < champion.distance) {
            champion.distance = distance;
            champion.point = node.point;
        }

        Node first, second;
        // choose first node to lookup: take that first which on the side of target
        if ((isV && target.x() < node.point.x()) || (!isV && target.y() < node.point.y())) {
            first = node.right;
            second = node.left;
        } else {
            first = node.left;
            second = node.right;
        }

        if (first != null && first.area.distanceSquaredTo(target) < champion.distance) {
            findNearest(target, first, !isV, champion);
        }
        if (second != null && second.area.distanceSquaredTo(target) < champion.distance) {
            findNearest(target, second, !isV, champion);
        }
    }

    public static void main(String[] args) {
    }

}
