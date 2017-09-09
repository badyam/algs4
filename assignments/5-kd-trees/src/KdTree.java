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
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private static class Node {
        private final Point2D point;      // the point
        private Node left;        // the left/bottom subtree
        private Node right;        // the right/top subtree

        private Node(Point2D point) {

            this.point = point;
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
            root = new Node(p);
            size = 1;
            return;
        }

        if (contains(p)) return;

        insert(root, true, p);
        size++;
    }

    private void insert(Node parent, boolean isV, Point2D point) {
        int cmp = isV
                ? Double.compare(point.x(), parent.point.x())
                : Double.compare(point.y(), parent.point.y());

        if (cmp > 0) { // left
            if (parent.left == null)
                parent.left = new Node(point);
            else
                insert(parent.left, !isV, point);
        } else { // right
            if (parent.right == null)
                parent.right = new Node(point);
            else
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

        return find(cmp > 0 ? node.left : node.right, !isV, point);
    }


    // draw all points to standard draw
    public void draw() {
        drawNode(root, new RectHV(0, 0, 1, 1), true);
    }

    private void drawNode(Node node, RectHV area, boolean isV) {
        if (node == null) return;

        RectHV leftArea, rightArea;
        // draw lines
        StdDraw.setPenRadius();
        if (isV) {
            rightArea = getRightArea(node.point, area);
            leftArea = getLeftArea(node.point, area);

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), leftArea.ymin(), node.point.x(), leftArea.ymax());
        } else {
            rightArea = getBottomArea(node.point, area);
            leftArea = getTopArea(node.point, area);

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(leftArea.xmin(), node.point.y(), leftArea.xmax(), node.point.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        drawNode(node.right, rightArea, !isV);
        drawNode(node.left, leftArea, !isV);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        final Queue<Point2D> result = new Queue<>();
        if (size == 0) return result;

        checkRect(root, rect, true, result);

        return result;
    }

    private void checkRect(Node node, RectHV rect, boolean isV, Queue<Point2D> result) {
        if (rect.contains(node.point))
            result.enqueue(node.point);

        // test location of rect relatively to the node sides to reduce lookups
        if (isV) { // test left / right sides
            if (node.right != null && rect.xmin() <= node.point.x())
                checkRect(node.right, rect, false, result);
            if (node.left != null && rect.xmax() > node.point.x())
                checkRect(node.left, rect, false, result);
        } else { // top (left) / bottom (right) sides
            if (node.right != null && rect.ymin() <= node.point.y())
                checkRect(node.right, rect, true, result);
            if (node.left != null && rect.ymax() > node.point.y())
                checkRect(node.left, rect, true, result);
        }
    }

    // a nearest neighbor in the set to point; null if the set is empty
    public Point2D nearest(Point2D target) {
        if (target == null) throw new IllegalArgumentException();
        if (size == 0) return null;

        NearestChampion champion = new NearestChampion();
        champion.distance = Double.MAX_VALUE;
        champion.point = null;

        findNearest(target, root, new RectHV(0, 0, 1, 1), true, champion);

        return champion.point;
    }

    private void findNearest(Point2D target, Node node, RectHV area, boolean isV, NearestChampion champion) {
        if (node == null) return;

        // make decision if the node is relevant to search in
        if (!isWorthArea(target, area, champion.distance)) return;

        double distance = target.distanceSquaredTo(node.point);
        if (distance < champion.distance) {
            champion.distance = distance;
            champion.point = node.point;
        }

        // cut off tree
        if (isV) {
            if (node.left != null) {
                findNearest(target, node.left, getLeftArea(node.point, area), false, champion);
            }
            if (node.right != null) {
                findNearest(target, node.right, getRightArea(node.point, area), false, champion);
            }
        } else {
            if (node.left != null) {
                findNearest(target, node.left, getTopArea(node.point, area), true, champion);
            }
            if (node.right != null) {
                findNearest(target, node.right, getBottomArea(node.point, area), true, champion);
            }
        }
    }

    private boolean isWorthArea(Point2D target, RectHV area, double championDistance) {
        if (area.contains(target)) return true;

        Point2D checkPoint1 = target.x() < area.xmin()
                ? new Point2D(area.xmin(), target.y()) // right border
                : new Point2D(area.xmax(), target.y()); // left border

        Point2D checkPoint2 = target.y() < area.ymin()
                ? new Point2D(target.x(), area.ymin())  // bottom border
                : new Point2D(target.x(), area.ymax()); // top border

        return target.distanceSquaredTo(checkPoint1) < championDistance ||
                target.distanceSquaredTo(checkPoint2) < championDistance;
    }

    private RectHV getRightArea(Point2D middlePoint, RectHV area) {
        return new RectHV(area.xmin(), area.ymin(), middlePoint.x(), area.ymax());
    }

    private RectHV getLeftArea(Point2D middlePoint, RectHV area) {
        return new RectHV(middlePoint.x(), area.ymin(), area.xmax(), area.ymax());
    }

    private RectHV getBottomArea(Point2D middlePoint, RectHV area) {
        return new RectHV(area.xmin(), area.ymin(), area.xmax(), middlePoint.y());
    }

    private RectHV getTopArea(Point2D middlePoint, RectHV area) {
        return new RectHV(area.xmin(), middlePoint.y(), area.xmax(), area.ymax());
    }


    public static void main(String[] args) {
        KdTree tree;

        tree = fixture("Construction");
        test("is empty", tree.isEmpty());
        test("size is zero", tree.size() == 0);

        tree = fixture("insert two points");
        tree.insert(new Point2D(0.5, 0.5));
        tree.insert(new Point2D(0.2, 0.2));
        test("is not empty", !tree.isEmpty());
        test("size is 2", tree.size() == 2);

        tree = fixture("input5 range");
        tree.insert(new Point2D(0.7, 0.2)); // A
        tree.insert(new Point2D(0.5, 0.4)); // B
        tree.insert(new Point2D(0.2, 0.3)); // C
        tree.insert(new Point2D(0.4, 0.7)); // D
        tree.insert(new Point2D(0.9, 0.6)); // E
        RectHV rect = new RectHV(0.21, 0.41, 0.4, 0.5);
        test("size is 5", tree.size() == 5);
        test("range() not null", tree.range(rect) != null);

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
