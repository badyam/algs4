/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class KdTreeVisualizer {

    public static void main(String[] args) {

        KdTree kdtree = new KdTree();
        if (args.length > 0) {

            String filename = args[0];
            In in = new In(filename);

            StdDraw.enableDoubleBuffering();
            StdDraw.setScale(-0.1, 1.1);
            StdDraw.setPenColor(Color.LIGHT_GRAY);
            StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);

            // initialize the data structures with N points from standard input
            int i = 0;
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdtree.insert(p);
                StdDraw.setPenColor(Color.DARK_GRAY);
                StdDraw.textRight(x + 0.03, y + 0.02, Character.toString((char) (65 + i++ % 255)));
            }
            kdtree.draw();

            StdDraw.setPenColor(Color.GREEN);
            StdDraw.setPenRadius();

            new RectHV(0.21, 0.41, 0.4, 0.5).draw();

            StdDraw.show();
        } else {
            RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            StdDraw.enableDoubleBuffering();
            while (true) {
                if (StdDraw.mousePressed()) {
                    double x = StdDraw.mouseX();
                    double y = StdDraw.mouseY();
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    Point2D p = new Point2D(x, y);
                    if (rect.contains(p)) {
                        StdOut.printf("%8.6f %8.6f\n", x, y);
                        kdtree.insert(p);
                        StdDraw.clear();
                        kdtree.draw();
                        StdDraw.show();
                    }
                }
                StdDraw.pause(50);
            }
        }
    }
}
