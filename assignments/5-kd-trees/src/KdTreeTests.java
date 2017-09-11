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
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeTests {


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

        tree = fixture("input5 range()");
        tree.insert(new Point2D(0.7, 0.2)); // A
        tree.insert(new Point2D(0.5, 0.4)); // B
        tree.insert(new Point2D(0.2, 0.3)); // C
        tree.insert(new Point2D(0.4, 0.7)); // D
        tree.insert(new Point2D(0.9, 0.6)); // E
        RectHV rect = new RectHV(0.21, 0.4, 0.41, 0.5);
        test("size is 5", tree.size() == 5);
        test("range() not null", tree.range(rect) != null);

        tree = fixture("input5 nearest()");
        tree.insert(new Point2D(0.7, 0.2)); // A
        tree.insert(new Point2D(0.5, 0.4)); // B
        tree.insert(new Point2D(0.2, 0.3)); // C
        tree.insert(new Point2D(0.4, 0.7)); // D
        tree.insert(new Point2D(0.9, 0.6)); // E
        Point2D nearest = tree.nearest(new Point2D(0.09, 0.53));
        Point2D expected = tree.nearest(new Point2D(0.2, 0.3));
        test("size is 5", tree.size() == 5);
        test("nearest is (0.2, 0.3)", nearest != null && nearest.equals(expected));
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
