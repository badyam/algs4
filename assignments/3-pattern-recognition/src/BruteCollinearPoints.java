/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/23/2017
 *  Last updated:  8/23/2017
 *
 *  Compilation:   javac BruteCollinearPoints.java
 *  Execution:     java BruteCollinearPoints
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final Bag<LineSegment> segmentBag;

    /**
     * Finds all line segments containing 4 points.
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // don't mutate input
        points = Arrays.copyOf(points, points.length);

        // to meet corner case requirement
        // "Throw a java.lang.IllegalArgumentException if the argument to the constructor contains a repeated point"
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        segmentBag = new Bag<>();
        final Point[] segment = new Point[4];

        // nice solution would be employ recursion for N, instead of fixed 4 loops
        for (int i = 0; i < points.length - 3; i++) {
            segment[0] = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException();
                }
                segment[1] = points[j];
                for (int k = j + 1; k < points.length - 1; k++) {
                    segment[2] = points[k];
                    for (int m = k + 1; m < points.length; m++) {
                        segment[3] = points[m];
                        double slope1 = segment[0].slopeTo(segment[1]);

                        double slope2 = segment[0].slopeTo(segment[2]);

                        double slope3 = segment[0].slopeTo(segment[3]);

                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope1, slope3) == 0) {
                            // collinear
                            segmentBag.add(buildLineSegment(segment));
                        }
                    }
                }
            }
        }
    }

    /**
     * The number of line segments.
     *
     * @return
     */
    public int numberOfSegments() {
        return segmentBag.size();
    }

    /**
     * The line segments
     *
     * @return
     */
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[segmentBag.size()];
        int i = 0;
        for (LineSegment lineSegment : segmentBag) {
            segments[i++] = lineSegment;
        }
        return segments;
    }

    private LineSegment buildLineSegment(Point[] input) {
        Point min = null;
        Point max = null;
        for (int i = 0; i < input.length; i++) {
            if (min == null || input[i].compareTo(min) < 0)
                min = input[i];
            if (max == null || input[i].compareTo(max) > 0)
                max = input[i];
        }
        return new LineSegment(min, max);
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1000, 32768);
        StdDraw.setYscale(-1000, 32768);
        StdDraw.setPenRadius(0.02);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.setPenRadius();
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}