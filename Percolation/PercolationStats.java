import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private final static double INTERVAL = 1.96;
    private final double[] fractionArray;
    private final double trialsRoot;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be positive");
        }
        this.trialsRoot = Math.sqrt((double) trials);
        this.fractionArray = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            this.fractionArray[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.fractionArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.fractionArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - INTERVAL * stddev() / this.trialsRoot;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + INTERVAL * stddev() / this.trialsRoot;
    }

    // test client
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        //      StdOut.println("Percolation with weightedQuickUnionUF takes " + stopwatch.elapsedTime() + "s");
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}
