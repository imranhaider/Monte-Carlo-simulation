/* *****************************************************************************
 *  Name:Imran Haider Ashraf
 *  Date:24th December, 2018
 *  Description: Percolation stats API
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double FACTOR = 1.96;
    private double[] values;
    private final int trials;
    private final int gridSize;
    private double mean = Double.NaN;
    private double stddev = Double.NaN;

    /**
     * perform trials independent experiments on an n-by-n grid
     *
     * @param n      represents grid size
     * @param trials represents trials count
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n");
        }

        if (trials <= 0) {
            throw new IllegalArgumentException("trials");
        }

        this.gridSize = n;
        this.trials = trials;
        this.values = new double[trials];
        for (int i = 0; i < trials; i++) {
            this.values[i] = 0;
        }

        this.runTrials();
    }

    /**
     * Runs trials and store threshold values
     */
    private void runTrials() {
        for (int i = 0; i < this.trials; i++) {
            Percolation percolation = new Percolation(this.gridSize);
            do {
                int row = StdRandom.uniform(1, this.gridSize + 1);
                int col = StdRandom.uniform(1, this.gridSize + 1);
                percolation.open(row, col);
            } while (!percolation.percolates());

            this.values[i] =
                    percolation.numberOfOpenSites() / Math.pow(gridSize, 2);
        }

    }

    /**
     * returns mean of percolation threshold
     *
     * @return mean
     */
    public double mean() {
        if (Double.isNaN(this.mean)) {
            this.mean = StdStats.mean(this.values);
        }

        return mean;
    }

    /**
     * standard deviation of percolation threshold
     *
     * @return standard deviation
     */
    public double stddev() {
        if (this.trials == 1) {
            return Double.NaN;
        }

        if (Double.isNaN(this.stddev)) {
            this.stddev = StdStats.stddev(this.values);
        }

        return this.stddev;
    }

    /**
     * returns low  endpoint of 95% confidence interval
     *
     * @return low confidence level
     */
    public double confidenceLo() {
        return this.mean() - ((FACTOR * this.stddev()) / Math.sqrt(this.trials));
    }

    /**
     * returns high endpoint of 95% confidence interval
     *
     * @return high confidence level
     */
    public double confidenceHi() {
        return this.mean() + ((FACTOR * this.stddev()) / Math.sqrt(this.trials));
    }

    /**
     * Test client
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        int gridSize = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(gridSize, trials);
        StdOut.print("mean :    " + stats.mean());
        StdOut.print("stddev :    " + stats.stddev());
        // StdOut.printf(
        //         "mean                    = {0}\n"
        //                 + "stddev                  = {1}\n"
        //                 + "95% confidence interval = [{2}, {3}]",
        //         stats.mean(), stats.stddev(),
        //         stats.confidenceLo(), stats.confidenceHi());
    }
}

