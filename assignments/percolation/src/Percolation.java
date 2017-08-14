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

    private static final byte TOP_CONNECTED = 2;

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

        // Checkstyle warns with 'The numeric literal '4' appears to be unnecessary', so inlined
        // final byte BOTTOM_CONNECTED = 4;
        // final byte PERCOLATED = 6;

        int status = 1; // OPEN

        if (row == 1) {
            status = status | TOP_CONNECTED;
        }

        if (row == n) {
            status = status | 4;
        }

        // look around and connect to other open sites
        byte[] neighborStates = new byte[4];
        for (int i = -1; i <= 1; i += 2) {
            // vertical neighbors (top, bottom)
            int y = row + i;
            if (y >= 1 && y <= n) {
                int neighborId = getId(y, col);
                if (state[neighborId] != 0) {
                    neighborStates[i + 1] = state[connections.find(neighborId)];
                    connections.union(siteId, neighborId);
                }
            }

            // horizontal neighbors (left, right)
            int x = col + i;
            if (x >= 1 && x <= n) {
                int neighborId = getId(row, x);
                if (state[neighborId] != 0) {
                    neighborStates[i + 2] = state[connections.find(neighborId)];
                    connections.union(siteId, neighborId);
                }
            }
        }

        // compute new site status
        // Checkstyle info: Using a loop in this method might be a performance bug.
        // for (byte rootState : neighborsRootStates) {
        //     status = status | rootState;
        // }
        // inlined
        status = status | neighborStates[0] | neighborStates[1] | neighborStates[2] | neighborStates[3];

        // update
        byte newState = (byte) status;
        state[siteId] = newState;
        state[connections.find(siteId)] = newState;

        if (!percolates) {
            percolates = (status & 6) == 6;
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

        p = new Percolation(2);
        p.open(1, 1);
        StdOut.printf("2x2 does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(1, 2);
        p.open(2, 1);
        p.open(2, 2);
        StdOut.printf("2x2 percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // bottom up
        p = new Percolation(2);
        p.open(2, 1);
        StdOut.printf("2x2 does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(1, 1);
        StdOut.printf("2x2 percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // top bottom
        p = new Percolation(3);
        p.open(1, 3);
        p.open(2, 3);
        StdOut.printf("does not percolate %s\n", !p.percolates() ? "OK" : "FAIL");
        p.open(3, 3);
        StdOut.printf("top bottom percolates %s\n", p.percolates() ? "OK" : "FAIL");

        p = new Percolation(3);
        p.open(1, 1);
        p.open(2, 1);
        p.open(3, 1);
        StdOut.printf("top bottom percolates %s\n", p.percolates() ? "OK" : "FAIL");

        // bottom up
        p = new Percolation(3);
        p.open(3, 2);
        p.open(2, 2);
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
        p = new Percolation(3);
        p.open(2, 2);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 2);
        StdOut.printf("complex path percolates %s\n", p.percolates() ? "OK" : "FAIL");
        StdOut.printf("numberOfOpenSites %s\n", p.numberOfOpenSites() == 4 ? "OK" : "FAIL");

    }

}
