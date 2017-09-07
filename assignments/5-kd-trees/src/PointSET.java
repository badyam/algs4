/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       9/6/2017
 *  Last updated:  9/6/2017
 *
 *  Compilation:   javac PointSET.java
 *  Execution:     java PointSET
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private final SET<Point2D> set = new SET<>();

    // construct an empty set of points
    public PointSET() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!set.contains(p)) set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-0.1, 1.1);
        StdDraw.setYscale(-0.1, 1.1);
        StdDraw.setPenRadius(0.02);
        for (Point2D p : set) {
            p.draw();
        }
        StdDraw.setPenRadius();
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        final Bag<Point2D> result = new Bag<>();
        for (Point2D p : set) {
            if (rect.contains(p)) result.add(p);
        }

        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D target) {
        if (target == null) throw new IllegalArgumentException();

        Point2D np = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D p : set) {
            double distance = p.distanceTo(target);
            if (distance < minDistance) {
                np = p;
                minDistance = distance;
            }
        }

        return np;
    }

    public static void main(String[] args) {
        PointSET pointSET;
        int v;

        pointSET = fixture("Construction");
        test("is empty", pointSET.isEmpty());
        test("size is zero", pointSET.size() == 0);

        pointSET = fixture("insert");
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.2, 0.2));
        test("is not empty", !pointSET.isEmpty());
        test("size is 1", pointSET.size() == 2);

        pointSET = fixture("nearest");
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.2, 0.2));
        Point2D nearest = pointSET.nearest(new Point2D(0.3, 0.3));
        test("0.2 is the nearest", nearest.equals(new Point2D(0.2, 0.2)));

        pointSET = fixture("range");
        pointSET.insert(new Point2D(0.5, 0.5));
        pointSET.insert(new Point2D(0.3, 0.3));
        pointSET.insert(new Point2D(0.2, 0.2));
        Iterable<Point2D> result = pointSET.range(new RectHV(0.25, 0.25, 0.6, 0.6));

        int i = 0;
        Point2D[] points = new Point2D[pointSET.size()];
        for (Point2D p: result) {
            points[i++] = p;
        }
        test("two points", i == 2);
        test("(0.5, 0.5)", points[0].equals(new Point2D(0.5, 0.5)));
        test("(0.3, 0.3)", points[1].equals(new Point2D(0.3, 0.3)));
    }

    private static PointSET fixture(String description) {
        StdOut.println();
        StdOut.println(description);
        return new PointSET();
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-32s %s\n", description, assertion ? "PASSED" : "FAILED");
    }

}
