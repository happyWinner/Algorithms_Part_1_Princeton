public class PercolationStats {

    private int N;       // grid size
    private int T;       // number of experiments
    private double[] vacancyRatios;

    // Perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        this.T = T;
        vacancyRatios = new double[T];

        for (int i = 0; i < T; ++i) {
            Percolation percolation = new Percolation(N);
            double vacancyNumber = 0;
            while (!percolation.percolates()) {
                int x = StdRandom.uniform(1, N + 1);
                int y = StdRandom.uniform(1, N + 1);
                if (!percolation.isOpen(x, y)) {
                    percolation.open(x, y);
                    ++vacancyNumber;
                }
            }
            vacancyRatios[i] = vacancyNumber / (N * N);
        }
    }

    // Sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(vacancyRatios);
    }

    // Sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(vacancyRatios);
    }

    // Returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // Returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // Test client
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(N, T);

        StdOut.printf("%-23s", "mean");
        StdOut.println(" = " + percolationStats.mean());
        StdOut.printf("%-23s", "stddev");
        StdOut.println(" = " + percolationStats.stddev());
        StdOut.printf("%-23s", "95% confidence interval");
        StdOut.println(" = " + percolationStats.confidenceLo()
                + ", " + percolationStats.confidenceHi());
    }
}
