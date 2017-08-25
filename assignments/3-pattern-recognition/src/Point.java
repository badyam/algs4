/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/23/2017
 *  Last updated:  8/23/2017
 *
 *  Compilation:   javac Point.java
 *  Execution:     java Point
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *----------------------------------------------------------------*/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param q the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point q) {
        // AutoGrader tests expect NullPointerException if argument(s) is null
        // PMD: Do not throw a 'NullPointerException' in this program. [AvoidThrowingNullPointerException]
//        if (q == null) {
//            throw new NullPointerException();
//        }

        if (x == q.x) {
            return y == q.y ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }

        if (y == q.y) {
            return +0;
        }

        return ((double) q.y - y) / (q.x - x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param q the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point q) {
// AutoGrader tests expect NullPointerException if argument(s) is null
// PMD: Do not throw a 'NullPointerException' in this program. [AvoidThrowingNullPointerException]
//        if (q == null) {
//            throw new NullPointerException();
//        }

        if (x == q.x && y == q.y) return 0;
        if (y < q.y || y == q.y && x < q.x) return -1;
        return +1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    private class SlopeComparator implements Comparator<Point> {
        public int compare(Point q, Point r) {
// AutoGrader tests expect NullPointerException if argument(s) is null
// PMD: Do not throw a 'NullPointerException' in this program. [AvoidThrowingNullPointerException]
//            if (q == null) {
//                throw new NullPointerException("p1 has to be not null");
//            }
//            if (r == null) {
//                throw new NullPointerException("p2 has be to not null");
//            }

            final double slope1 = Point.this.slopeTo(q);
            final double slope2 = Point.this.slopeTo(r);

            return Double.compare(slope1, slope2);
        }
    }

    public static void main(String[] args) {
        Point p;
        Point q;
        Point r;

        fixture("Slope special checks");
        p = new Point(147, 426);
        q = new Point(130, 426);
        test("positive zero", p.slopeTo(q) == +0.0);

        fixture("p.slopeOrder().compare(q, r)");
        p = new Point(2, 1);
        q = new Point(5, 1);
        r = new Point(1, 1);
        test("positive zero", p.slopeOrder().compare(q, r) == +0.0);

        fixture("Slope");
        p = new Point(1, 1);

        q = new Point(p.x, p.y);
        test("identical points slope is Double.NEGATIVE_INFINITY", p.slopeTo(q) == Double.NEGATIVE_INFINITY);

        q = new Point(p.x, p.y + 2);
        test("vertical line slope is Double.POSITIVE_INFINITY", p.slopeTo(q) == Double.POSITIVE_INFINITY);

        q = new Point(p.x + 1, p.y);
        test("horizontal line slope is +0.0", p.slopeTo(q) == 0.0);

        q = new Point(p.x + 1, p.y + 1);
        test("another point is above at right, slope is positive", p.slopeTo(q) > 0.0);

        q = new Point(p.x - 1, p.y + 1);
        test("another point is above at left, slope is negative", p.slopeTo(q) < 0.0);

        q = new Point(p.x + 1, p.y - 1);
        test("another point is below at right, slope is negative", p.slopeTo(q) < 0.0);

        q = new Point(p.x - 1, p.y - 1);
        test("another point is below at left, slope is positive", p.slopeTo(q) > 0.0);

        fixture("CompareTo another point");
        p = new Point(1, 1);

        q = new Point(p.x, p.y);
        test("identical points are equal", p.compareTo(q) == 0);

        q = new Point(p.x, p.y + 2);
        test("another point above - less", p.compareTo(q) < 0);

        q = new Point(p.x, p.y - 2);
        test("another point below - greater", p.compareTo(q) > 0);

        q = new Point(p.x + 1, p.y);
        test("another point at right - less", p.compareTo(q) < 0);

        q = new Point(p.x - 1, p.y);
        test("another point at left - greater", p.compareTo(q) > 0);

        q = new Point(p.x + 1, p.y - 2);
        test("another point below at right - greater", p.compareTo(q) > 0);

        q = new Point(p.x - 3, p.y - 2);
        test("another point below at left - greater", p.compareTo(q) > 0);

        q = new Point(p.x + 1, p.y + 2);
        test("another point above at right - less", p.compareTo(q) < 0);

        q = new Point(p.x - 3, p.y + 2);
        test("another point above at left - less", p.compareTo(q) < 0);
    }

    private static void fixture(String description) {
        StdOut.println();
        StdOut.println(description);
        StdOut.println("---------------------------");
    }

    private static void test(String description, boolean assertion) {
        StdOut.printf(" * %-56s %s\n", description, assertion ? "PASSED" : "FAILED");
    }
}
