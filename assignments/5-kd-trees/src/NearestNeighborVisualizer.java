/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-0.1, 1.1);

        // initialize the two data structures with point from standard input
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            StdDraw.clear();
            StdDraw.setPenColor(Color.GREEN);
            StdDraw.setPenRadius(0.03);
            query.draw();

            // draw all of the points
            StdDraw.setPenColor(Color.LIGHT_GRAY);
            StdDraw.setPenRadius();
            StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            Point2D bf = brute.nearest(query);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.textRight(query.x() + 0.03, query.y() - 0.03, Double.toString(query.distanceTo(bf)));
            StdDraw.setPenRadius(0.03);
            bf.draw();

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            Point2D kd = kdtree.nearest(query);
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.textRight(query.x() + 0.03, query.y() + 0.03, Double.toString(query.distanceTo(kd)));
            StdDraw.setPenRadius(0.02);
            kd.draw();


            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
