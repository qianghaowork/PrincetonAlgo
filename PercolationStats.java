import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private int trials; // number of trials
    private double[] threshsamp; // threshold samples

    // Performs T independent computational experiments on an N-by-N grid.
    public PercolationStats(int num, int tn) {
        if (num <= 0 || tn <= 0) {
            throw new IllegalArgumentException("Given N <= 0 || T <= 0");
        }
        
        trials = tn;
        threshsamp = new double[trials];
        for (int t = 0; t < trials; t++) {
            Percolation pr = new Percolation(num);
            int opencnt = 0;
            while (!pr.percolates()) {
                int i = StdRandom.uniform(1, num + 1);
                int j = StdRandom.uniform(1, num + 1);
                if (!pr.isOpen(i, j)) {
                    pr.open(i, j);
                    opencnt++;
                }
            }
            double ratio = (double) opencnt / (num * num);
            threshsamp[t] = ratio;
        }
    }

    // Mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshsamp);
    }

    // Standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(threshsamp);
    }


    // Return lower bound of the 95% confidence interval.
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
    }

    // Returns upper bound of the 95% confidence interval.
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
    }

    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        int tn = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(num, tn);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
