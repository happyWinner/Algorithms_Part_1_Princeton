public class Percolation {

    private boolean[] sitesStatus;
    private int N;
    private int virtualTop;
    private int virtualBottom;
    private WeightedQuickUnionUF sites;
    private WeightedQuickUnionUF sitesWithoutVirtualBottom;  // prevent backwash

    // Create N-by-N grid, with all sites blocked
    // The grid has a virtual top site and a virtual bottom site
    public Percolation(int N) {
        this.N = N;
        virtualTop = 0;
        virtualBottom = N * N + 1;
        sites = new WeightedQuickUnionUF(N * N + 2);
        sitesWithoutVirtualBottom = new WeightedQuickUnionUF(N * N + 1);

        sitesStatus = new boolean[N * N + 2];
        sitesStatus[virtualTop] = true;
        for (int i = 1; i < sitesStatus.length; ++i) {
            sitesStatus[i] = false;
        }
    }

    // Validate indices
    private void isValid(int i, int j) {
        if (i < 1 || i > N) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        else if (j < 1 || j > N) {
            throw new IndexOutOfBoundsException("column index j out of bounds");
        }
    }

    // Map from a 2-D pair to a 1-D union find object index
    private int xyTo1D(int i, int j) {
        isValid(i, j);
        return (i - 1) * N + j;
    }

    // Open site (row i, column j) if it is not already
    public void open(int i, int j) {
        int idx = xyTo1D(i, j);

        if (!isOpen(i, j)) {
            sitesStatus[idx] = true; 

            if (i == 1) {
                sites.union(virtualTop, idx);
                sitesWithoutVirtualBottom.union(virtualTop, idx);
            }
            else {
                if (isOpen(i - 1, j)) {
                    sites.union(idx - N, idx);
                    sitesWithoutVirtualBottom.union(idx - N, idx);
                }
            }

            if (i == N) {
                sites.union(virtualBottom, idx);
            }
            else {
                if (isOpen(i + 1, j)) {
                    sites.union(idx + N, idx);
                    sitesWithoutVirtualBottom.union(idx + N, idx);
                }
            }

            if (j != 1 && isOpen(i, j - 1)) {
                sites.union(idx - 1, idx);
                sitesWithoutVirtualBottom.union(idx - 1, idx);
            }

            if (j != N && isOpen(i, j + 1)) {
                sites.union(idx + 1, idx);
                sitesWithoutVirtualBottom.union(idx + 1, idx);
            }
        }
    }

    // Is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        int idx = xyTo1D(i, j);
        return sitesStatus[idx];
    }

    // Is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        int idx = xyTo1D(i, j);
        return sitesWithoutVirtualBottom.connected(idx, virtualTop);
    }

    // Does the system percolate?
    public boolean percolates() {
        return sites.connected(virtualTop, virtualBottom);
    }
}