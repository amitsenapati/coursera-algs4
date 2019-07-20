/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_96 = 1.96;

    private final double[] openSite;
    private final int trialsCnt;

    // perform independent trials on an n-by-n grid
    public PercolationStats(final int n, final int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        trialsCnt = trials;
        final double numberOfSites = n * n;
        openSite = new double[trials];
        for (int x = 0; x < trials; x++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);
                while (percolation.isOpen(i, j)) {
                    i = StdRandom.uniform(1, n + 1);
                    j = StdRandom.uniform(1, n + 1);
                }
                percolation.open(i, j);
                // System.out.println("site " + i + "," + j + " open");
            }
            openSite[x] = percolation.numberOfOpenSites() / numberOfSites;
            // System.out.println("iteration " + x + " number of open sites "
            //                            + percolation.numberOfOpenSites());
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(openSite);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(openSite);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_96 * stddev()) / Math.sqrt(trialsCnt);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_96 * stddev()) / Math.sqrt(trialsCnt);
    }

    // test client (see below)
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(size, iterations);
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + "[" +
                               stats.confidenceLo() + ", "
                               + stats.confidenceHi() + "]");
    }
}
