/*----------------------------------------------------------------
 *  Author:        Andrey Braynin
 *  Written:       8/10/2017
 *  Last updated:  8/10/2017
 *
 *  Compilation:   javac PercolationStats.java
 *  Execution:     java PercolationStats 20 100
 *
 *  http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

/**
 * Provides percolation statistics tests on NxN grid structure
 */
public final class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n < 1) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        
        if (trials < 1) {
            throw new IllegalArgumentException("trials must be greater than 0");
        }

        double[] thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            thresholds[i] = experiment(n);
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);

        double d = 1.96 * stddev / Math.sqrt(trials);
        confidenceLo = mean - d;
        confidenceHi = mean + d;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    private double experiment(int n) {
        Percolation p = new Percolation(n);
        do {
            int row = 1 + StdRandom.uniform(n);
            int col = 1 + StdRandom.uniform(n);
            p.open(row, col);
        } while (!p.percolates());

        return (double) p.numberOfOpenSites() / (n * n);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println("Two arguments of integer type expected");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, t);
        StdOut.printf("mean                    = %.16f\n", stats.mean());
        StdOut.printf("stdev                   = %.16f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]\n",
                stats.confidenceLo(), stats.confidenceHi());
    }
}
