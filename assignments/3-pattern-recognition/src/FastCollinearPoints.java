/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/23/2017
 *  Last updated:  8/23/2017
 *
 *  Compilation:   javac FastCollinearPoints.java
 *  Execution:     java FastCollinearPoints
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final Bag<LineSegment> segmentBag;

    /**
     * Finds all line segments containing 4 points.
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
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

        // check duplicates
        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        segmentBag = new Bag<>();
        final int lastIndex = points.length - 1;
        final int n = 3; // how many points must be collinear to another point (number of required collinear points - 1)

        // loop to length - n, because the last n points are processed inside the loop
        // if after sorting we have got N (4) or less not collinear points,
        // then we are not interested in further processing
        for (int i = 0; i < points.length - n; i++) {
            Point p = points[i];

            // sort the rest of array relatively to the start point by slope
            // essential: the toIndex parameter of Arrays.sort is not inclusive!
            Arrays.sort(points, i + 1, points.length, p.slopeOrder());

            double prevSlope = p.slopeTo(points[i + 1]);
            int segmentStart = i + 1;

            // number of same slopes
            int x = 1;
            for (int j = i + 2; j < points.length; j++) {
                double currSlope = p.slopeTo(points[j]);
                if (Double.compare(prevSlope, currSlope) == 0) {
                    x++;
                } else {
                    // process previous segment
                    if (x >= n) {
                        addSegment(points, i, segmentStart, j - 1, prevSlope);
                    }
                    prevSlope = currSlope;
                    segmentStart = j;
                    x = 1;
                }
            }

            // check last segment
            if (x >= n) {
                addSegment(points, i, segmentStart, lastIndex, prevSlope);
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

    private void addSegment(Point[] points, int startIndex, int fromIndex, int toIndex, double slope) {
        Point startPoint = points[startIndex];
        // check if one processed point ( i < startIndex) belongs to new segment
        for (int i = 0; i < fromIndex; i++) {
            if (Double.compare(points[i].slopeTo(points[startIndex]), slope) == 0) {
                // it's sub-segment
                return;
            }
        }
        Point min = startPoint;
        Point max = startPoint;
        for (int i = fromIndex; i <= toIndex; i++) {
            if (points[i].compareTo(min) < 0)
                min = points[i];
            if (points[i].compareTo(max) > 0)
                max = points[i];
        }
        segmentBag.add(new LineSegment(min, max));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}