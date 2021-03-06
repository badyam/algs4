/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/10/2017
 *  Last updated:  8/10/2017
 *
 *  Compilation:   javac Percolation.java
 *  Execution:     java Percolation
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Implements percolation test for n-by-n grid structure
 */
public class Percolation {

    private static final byte OPEN = 1;
    private static final byte TOP_CONNECTED = 2;
    private static final byte BOTTOM_CONNECTED = 4;
    private static final byte PERCOLATED = TOP_CONNECTED | BOTTOM_CONNECTED;

    private final int n;

    private int numberOfOpenSites;

    private final byte[] state;

    private final WeightedQuickUnionUF connections;

    private boolean percolates;

    /**
     * Creates n-by-n grid, with all sites blocked.
     * @param n
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.n = n;
        state = new byte[n * n];
        connections = new WeightedQuickUnionUF(n * n);

        numberOfOpenSites = 0;
    }

    /**
     * Open site if it is not open already.
     */
    public void open(int row, int col) {
        validatePosition(row, col);

        int siteId = getId(row, col);

        if (state[siteId] != 0) {
            return;
        }

        numberOfOpenSites++;

        int siteState = OPEN;

        if (row == 1) {
            siteState = siteState | TOP_CONNECTED;
        }

        if (row == n) {
            siteState = siteState | BOTTOM_CONNECTED;
        }

        // look around
        // and connect to other open sites
        // matrix flattening
        //  -4  -3  -2
        //  -1   0   1
        //   2   3   4
        // flatten range: [-3, -1,  1,  3]
        // dx = i % 3; // [ 0, -1,  1,  0]
        // dy = i / 2; // [-1,  0,  0,  1]
        for (int i = -3; i <= 3; i += 2) {
            int dy = i % 3;
            int dx = i / 2;
            int y = row + dy;
            int x = col + dx;
            if (y >= 1 && y <= n && x >= 1 && x <= n) {
                int neighborId = getId(y, x);
                if (state[neighborId] != 0) {
                    // mixin state of neighbor before union into site state
                    siteState = siteState | state[connections.find(neighborId)];
                    connections.union(siteId, neighborId);
                }
            }
        }

        // update
        byte newState = (byte) siteState;
        int siteComponentId = connections.find(siteId);
        if (siteComponentId == 0) {
            state[siteId] = newState;
        } else {
            state[siteComponentId] = newState;
        }

        if (!percolates) {
            percolates = (siteState & PERCOLATED) == PERCOLATED;
        }
    }

    /**
     * Examines if site is open.
     */
    public boolean isOpen(int row, int col) {
        validatePosition(row, col);

        return state[getId(row, col)] > 0;
    }

    /**
     * Examines if site is full.
     */
    public boolean isFull(int row, int col) {
        validatePosition(row, col);

        int siteId = getId(row, col);
        return ((int) state[connections.find(siteId)] & TOP_CONNECTED) == TOP_CONNECTED;
    }

    /**
     * Get number of open sites
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * Examines if the system percolates.
     */
    public boolean percolates() {
        return percolates;
    }

    private void validatePosition(int row, int col) {
        if (row <= 0 || row > n) {
            throw new IllegalArgumentException();
        }
        if (col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    // Gets site Id in connections
    // row, col are internal coordinates
    private int getId(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    // unit tests
    public static void main(final String[] args) {
        Percolation p = new Percolation(1);
        StdOut.printf("1x1 does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(1, 1);
        StdOut.printf("1x1 percolates %s\n", p.percolates() ? "OK" : "FAIL");
        StdOut.printf("1x1 is full %s\n", p.isFull(1,1) ? "OK" : "FAIL");

        p = new Percolation(2);
        p.open(1, 1);
        StdOut.printf("2x2 does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(1, 2);
        p.open(2, 1);
        p.open(2, 2);
        StdOut.printf("2x2 percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // top bottom
        p = new Percolation(3);
        p.open(1, 3);
        p.open(2, 3);
        StdOut.printf("does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(3, 3);
        StdOut.printf("top bottom percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // bottom up
        p = new Percolation(3);
        p.open(3, 2);
        p.open(2, 2);
        StdOut.printf("does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(1, 2);
        StdOut.printf("bottom up percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // middle bottom top
        p = new Percolation(3);
        p.open(2, 2);
        p.open(3, 2);
        p.open(1, 2);
        StdOut.printf("middle bottom top percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // middle top bottom
        p = new Percolation(3);
        p.open(2, 2);
        p.open(1, 2);
        p.open(3, 2);
        StdOut.printf("middle top bottom percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // complex path
        StdOut.println("complex path:");
        p = new Percolation(4);
        p.open(2, 2);
        p.open(2, 3);
        p.open(1, 3);
        StdOut.printf("\tis full %s\n", p.isFull(2,2) ? "OK" : "FAIL");
        StdOut.printf("\tis full %s\n", p.isFull(2,3) ? "OK" : "FAIL");
        StdOut.printf("\tis full %s\n", p.isFull(1,3) ? "OK" : "FAIL");
        StdOut.printf("\tdoes not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(3, 4);
        StdOut.printf("\tis full %s\n", !p.isFull(3,4) ? "OK" : "FAIL");
        p.open(2, 4);
        StdOut.printf("\tis full %s\n", p.isFull(3,4) ? "OK" : "FAIL");
        StdOut.printf("\tdoes not percolate %s\n", !p.percolates() ? "OK" : "FAIL");

        p.open(4, 1);
        StdOut.printf("\tis full %s\n", !p.isFull(4,1) ? "OK" : "FAIL");

        p.open(4, 4);
        StdOut.printf("\tpercolates %s\n", p.percolates() ? "OK" : "FAIL");
        StdOut.printf("\tbackwash %s\n", !p.isFull(4,1) ? "OK" : "FAIL");
        StdOut.printf("\tnumberOfOpenSites %s\n", p.numberOfOpenSites() == 7 ? "OK" : "FAIL");
    }

}
