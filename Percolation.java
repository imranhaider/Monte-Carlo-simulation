/* *****************************************************************************
 *  Name:Imran Haider Ashraf
 *  Date:24th December, 2018
 *  Description: Percolation API
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private boolean[] fullMarkers;
    private final int gridSize;
    private int count;
    // private boolean isPercolates;
    private final WeightedQuickUnionUF unionFind;

    /**
     * Initializes grid with all sites blocked
     *
     * @param gridSize size of the grid
     */
    public Percolation(int gridSize) {

        if (gridSize <= 0) {
            throw new IllegalArgumentException("gridSize");
        }

        this.gridSize = gridSize;
        this.grid = new boolean[gridSize + 1][gridSize + 1];
        this.unionFind = new WeightedQuickUnionUF(grid.length * grid.length);
        this.fullMarkers = new boolean[grid.length * grid.length];
        for (int i = 1; i <= gridSize; i++) {
            for (int j = 1; j <= gridSize; j++) {
                this.grid[i][j] = false;
            }
        }
    }


    /**
     * Opens a site on given coordinates
     *
     * @param row represents a row
     * @param col represents a column
     */
    public void open(int row, int col) {
        this.validateSite(row, col);

        if (this.isOpen(row, col)) {
            return;
        }

        this.grid[row][col] = true;
        this.count++;
        this.connectNeighbors(row, col);
    }

    /**
     * connects neighboring sites if they are already open
     *
     * @param row represents a row
     * @param col represents a column
     */
    private void connectNeighbors(int row, int col) {
        this.markFull(row, col, false);
        this.connectSites(row, col, row - 1, col);
        this.connectSites(row, col, row + 1, col);
        this.connectSites(row, col, row, col - 1);
        this.connectSites(row, col, row, col + 1);
    }


    /**
     * connects two sites
     *
     * @param row  represents a row of a main site
     * @param col  represents a columns of a main site
     * @param row1 represetns a row of a connecting site
     * @param col1 represents a column of a connecting site
     */
    private void connectSites(int row, int col, int row1, int col1) {
        if (this.isValidIndex(row1) && this.isValidIndex(col1)) {
            if (!this.isOpen(row1, col1)) {
                return;
            }

            this.markFull(row, col, this.fullMarkers[this.xyTo1D(row1, col1)]);
            this.markFull(row1, col1, this.fullMarkers[this.xyTo1D(row, col)]);

            this.unionFind.union(this.xyTo1D(row, col),
                                 this.xyTo1D(row1, col1));
            // Marking root of larger unmarked tree if smaller tree root is full
            if (this.isFull(row, col)) {
                this.fullMarkers[this.unionFind.find(this.xyTo1D(row1, col1))]
                        = true;
            }
            else if (this.isFull(row1, col1)) {
                this.fullMarkers[this.unionFind.find(this.xyTo1D(row, col))]
                        = true;
            }
        }

    }

    private void markFull(int row, int col, boolean fullFlag) {
        if (fullFlag || row == 1 || this.isFull(row, col)) {
            int root = this.unionFind.find(this.xyTo1D(row, col));
            this.fullMarkers[this.xyTo1D(row, col)] = true;
            this.fullMarkers[root] = true;
        }
    }


    /**
     * checks if a site is already opened
     *
     * @param row represents a row
     * @param col represents a column
     * @return boolean
     */
    public boolean isOpen(int row, int col) {

        this.validateSite(row, col);

        return this.grid[row][col];
    }

    /**
     * checks if a site connects with a top row through chain of neighbors
     *
     * @param row represents a row
     * @param col represents a column
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        this.validateSite(row, col);

        if (!this.isOpen(row, col)) {
            return false;
        }

        return this.fullMarkers[this.xyTo1D(row, col)] ||
                this.fullMarkers[this.unionFind.find(this.xyTo1D(row, col))];
    }

    /**
     * checks if given grid percolates
     *
     * @return boolean
     */
    public boolean percolates() {
        for (int i = 1; i <= this.gridSize; i++) {
            if (!this.isOpen(this.gridSize, i)) {
                continue;
            }


            if (this.isFull(this.gridSize, i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * return a count of open sites
     *
     * @return integer
     */
    public int numberOfOpenSites() {
        return this.count;
    }

    /**
     * Returns integer value to represent a cell
     *
     * @param row represents a row
     * @param col represents a column
     * @return integer value
     */
    private int xyTo1D(int row, int col) {
        return this.gridSize * row + col;
    }

    /**
     * validates a site and throws a IllegalArgumentException if not
     *
     * @param row represents a row
     * @param col represents a column
     */
    private void validateSite(int row, int col) {
        if (!this.isValidIndex(row)) {
            throw new IllegalArgumentException("row: " + row);
        }

        if (!this.isValidIndex(col)) {
            throw new IllegalArgumentException("col: " + col);
        }
    }

    /**
     * validates if an index is within bounds
     *
     * @param index an index
     * @return boolean
     */
    private boolean isValidIndex(int index) {
        return index >= 1 && index <= this.gridSize;
    }
}
