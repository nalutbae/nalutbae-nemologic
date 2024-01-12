package com.story4g.nalutbae.nemologic;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Solving Nemo Logic
 *
 * @author nalutbae
 */
public class NemoLogicSolver {

    private static final char EMPTY = '□';
    private static final char FILLED = '■';

    private final int rowSize; // Size of total rows
    private final int columnSize; // size of total columns
    private final int[][] rowsHints; // rows hints
    private final int[][] columnHints; // columns hints
    private final long[] grid;
    private long[][] rowPermutations; // adjacency matrix
    private int[][] columnValue;
    private int[][] columnIndex;
    private long[] mask;
    private long[] val;

    /**
     * Constructor, initialized by receiving row/column hints
     *
     * @param rowsHints Arrangement of the number of consecutive colored cells per row.
     * @param columnHints Arrangement of the number of consecutive colored cells per column.
     */
    public NemoLogicSolver(int[][] rowsHints, int[][] columnHints) {
        this.rowSize = rowsHints.length;
        this.columnSize = columnHints.length;
        this.rowsHints = rowsHints;
        this.columnHints = columnHints;
        this.grid = new long[rowSize];
    }

    /**
     * processing nemonemo logic solving
     */
    public void process() {
        OutputWriter out = new OutputWriter(System.out);
        initializeRowPermutations();

        columnValue = new int[rowSize][columnSize];
        columnIndex = new int[rowSize][columnSize];
        mask = new long[rowSize];
        val = new long[rowSize];

        if (dfs(0)) {
            printGrid(out);
        } else {
            out.printLine("Sorry. I can't not solve the answer");
        }
        out.close();
    }

    private void initializeRowPermutations() {
        rowPermutations = new long[rowSize][];
        for (int r = 0; r < rowSize; r++) {
            LinkedList<Long> resolves = new LinkedList<>();
            int spaces = columnSize - (rowsHints[r].length - 1);
            for (int i = 0; i < rowsHints[r].length; i++) {
                spaces -= rowsHints[r][i];
            }
            calculatePerms(r, 0, spaces, 0, 0, resolves);
            if (resolves.isEmpty()) {
                throw new RuntimeException("Could not found the solution for " + r + "th row.");
            }

            int size = resolves.size();
            rowPermutations[r] = new long[size];
            Iterator<Long> iterator = resolves.descendingIterator();
            for (int i = 0; i < size; i++) {
                rowPermutations[r][i] = iterator.next();
            }
        }
    }

    private boolean dfs(int row) {
        if (row == rowSize) {
            return true;
        }

        rowMask(row); // Calculate the valid mask on the next line

        for (long permutation : rowPermutations[row]) {
            if ((permutation & mask[row]) != val[row]) {
                continue;
            }
            grid[row] = permutation;
            updateColumns(row);
            if (dfs(row + 1)) {
                return true;
            }
        }

        return false;
    }

    private void rowMask(int row) {
        mask[row] = val[row] = 0;
        if (row > 0) {
            long ixc = 1L;
            for (int c = 0; c < columnSize; c++, ixc <<= 1) {
                if (columnValue[row - 1][c] > 0) {
                    mask[row] |= ixc;
                    if (columnHints[c][columnIndex[row - 1][c]] > columnValue[row - 1][c]) {
                        val[row] |= ixc; // must set
                    }
                } else if (columnValue[row - 1][c] == 0 && columnIndex[row - 1][c] == columnHints[c].length) {
                    mask[row] |= ixc;
                }
            }
        }
    }

    private void updateColumns(int row) {
        long indexColumn = 1L;
        for (int c = 0; c < columnSize; c++, indexColumn <<= 1) {
            // copy previous value
            columnValue[row][c] = (row == 0) ? 0 : columnValue[row - 1][c];
            columnIndex[row][c] = (row == 0) ? 0 : columnIndex[row - 1][c];
            if ((grid[row] & indexColumn) == 0) {
                if (row > 0 && columnValue[row - 1][c] > 0) {
                    // If the bit is not set and the previous column is not empty, it is treated as 0.
                    columnValue[row][c] = 0;
                    columnIndex[row][c]++;
                }
            } else {
                columnValue[row][c]++; // bit value increase
            }
        }
    }

    private void calculatePerms(int r, int cur, int spaces, long perm, int shift, LinkedList<Long> res) {
        if (cur == rowsHints[r].length) {
            if ((grid[r] & perm) == grid[r]) {
                res.add(perm);
            }
            return;
        }
        while (spaces >= 0) {
            calculatePerms(r, cur + 1, spaces, perm | (bits(rowsHints[r][cur]) << shift), shift + rowsHints[r][cur] + 1, res);
            shift++;
            spaces--;
        }
    }

    private long bits(int b) {
        return (1L << b) - 1;
    }

    private void printGrid(OutputWriter out) {
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < columnSize; c++) {
                out.print((grid[r] & (1L << c)) == 0 ? EMPTY : FILLED);
            }
            out.printLine();
        }
    }

    /**
     * System Console Output Purpose
     */
    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void print(Object... objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0)
                    writer.print(' ');
                writer.print(objects[i]);
            }
        }

        public void printLine(Object... objects) {
            print(objects);
            writer.println();
        }

        public void close() {
            writer.close();
        }
    }
}
