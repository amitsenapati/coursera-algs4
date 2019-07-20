/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private boolean[][] model;
    private final int size;
    private int openSiteCount;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        model = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                model[i][j] = false;
            }
        }
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        size = n;

        for (int i = 1; i <= n; i++) {
            weightedQuickUnionUF.union(0, encode(1, i));
            weightedQuickUnionUF.union(n * n + 1, encode(n, i));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!validate(row) || !validate(col))
            throw new IllegalArgumentException();
        if (!isOpen(row, col)) {
            model[row - 1][col - 1] = true;
            openSiteCount += 1;
            if (row < size && isOpen(row + 1, col))
                weightedQuickUnionUF.union(encode(row + 1, col), encode(row, col));
            if (row > 1 && isOpen(row - 1, col))
                weightedQuickUnionUF.union(encode(row-1, col), encode(row, col));
            if (col < size && isOpen(row, col + 1))
                weightedQuickUnionUF.union(encode(row, col + 1), encode(row, col));
            if (col > 1 && isOpen(row, col - 1))
                weightedQuickUnionUF.union(encode(row, col -1), encode(row, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!validate(row) || !validate(col))
            throw new IllegalArgumentException();
        return model[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!validate(row) || !validate(col))
            throw new IllegalArgumentException();
        return isOpen(row, col) && weightedQuickUnionUF.connected(0, encode(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, size * size + 1);
    }

    private int encode(int row, int col) {
        return ((row - 1) * size) + col;
    }

    private boolean validate(int p) {
        if (p < 1 || p > size)
            return false;
        return true;
    }

    // test client (optional)
    public static void main(String[] args) {
        int size = StdIn.readInt();
        Percolation percolation = new Percolation(size);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
        }
    }
}
