import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int length;
    private boolean[] gridFlag;
    private boolean[] connectTop;
    private boolean[] connectBottom;
    private final WeightedQuickUnionUF uf;
    private int sum;
    private boolean percolateFlag;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        this.length = n;
        int size = n * n;
        this.gridFlag = new boolean[size];
        this.connectTop = new boolean[size];
        this.connectBottom = new boolean[size];
        this.uf = new WeightedQuickUnionUF(size);
        this.sum = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowCol(row, col);
        int index = getIndex(row, col);
        if (!this.gridFlag[index]) {
            this.gridFlag[index] = true;
            this.sum++;
        }
        boolean top = false;
        boolean bottom = false;

        if (row < this.length && this.gridFlag[index + this.length]) {
            if (connectTop[uf.find(index + this.length)] || connectTop[uf.find(index)]) {
                top = true;
            }
            if (connectBottom[uf.find(index + this.length)] || connectBottom[uf.find(index)]) {
                bottom = true;
            }
            uf.union(index, index + this.length);
        }

        if (row > 1 && this.gridFlag[index - this.length]) {
            if (connectTop[uf.find(index - this.length)] || connectTop[uf.find(index)]) {
                top = true;
            }
            if (connectBottom[uf.find(index - this.length)] || connectBottom[uf.find(index)]) {
                bottom = true;
            }
            uf.union(index, index - this.length);
        }

        if (col < this.length && this.gridFlag[index + 1]) {
            if (connectTop[uf.find(index + 1)] || connectTop[uf.find(index)]) {
                top = true;
            }
            if (connectBottom[uf.find(index + 1)] || connectBottom[uf.find(index)]) {
                bottom = true;
            }
            uf.union(index, index + 1);
        }

        if (col > 1 && this.gridFlag[index - 1]) {
            if (connectTop[uf.find(index - 1)] || connectTop[uf.find(index)]) {
                top = true;
            }
            if (connectBottom[uf.find(index - 1)] || connectBottom[uf.find(index)]) {
                bottom = true;
            }
            uf.union(index, index - 1);
        }
        if (row == 1) {
            top = true;
        }
        if (row == this.length) {
            bottom = true;
        }
        connectTop[uf.find(index)] = top;
        connectBottom[uf.find(index)] = bottom;
        if (connectTop[uf.find(index)] && connectBottom[uf.find(index)]) {
            percolateFlag = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return this.gridFlag[getIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        return connectTop[uf.find(getIndex(row, col))];
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.sum;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolateFlag;
    }

    private void checkRowCol(int row, int col) {
        if (row < 1 || row > this.length) {
            throw new IllegalArgumentException("row is out of boundary");
        }
        if (col < 1 || col > this.length) {
            throw new IllegalArgumentException("col is out of boundary");
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * this.length + col - 1;
    }
}
