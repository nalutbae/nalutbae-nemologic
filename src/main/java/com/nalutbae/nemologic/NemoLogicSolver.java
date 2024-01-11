package com.nalutbae.nemologic;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;

/**
 * Solving Nemo Logic
 *
 * @author nalutbae
 */
public class NemoLogicSolver {

    private int ROW_SIZE, COLUMN_SIZE;            // Size of total rows/columns
    private int[][] rowsHints, columnHints;        // rows/columns hints
    private long[] grid;
    private long[][] rowPermutations;            // adjacency matrix
    private final char EMPTY = '□', FILLED = '■';

    private int[][] columnValue, columnIndex;
    private long[] mask, val;

    /**
     * Constructor, initialized by receiving row/column hints
     *
     * @param rowsHints
     * @param columnHints
     */
    public NemoLogicSolver(int[][] rowsHints, int[][] columnHints) {
        this.ROW_SIZE = rowsHints.length;
        this.COLUMN_SIZE = columnHints.length;
        this.rowsHints = rowsHints;
        this.columnHints = columnHints;
        this.grid = new long[ROW_SIZE];
    }

    public void process() throws Exception {
        final OutputWriter out = new OutputWriter(System.out);

        rowPermutations = new long[ROW_SIZE][];
        for (int r = 0; r < ROW_SIZE; r++) {
            LinkedList<Long> resolves = new LinkedList<Long>();
            int spaces = COLUMN_SIZE - (rowsHints[r].length - 1);
            for (int i = 0; i < rowsHints[r].length; i++) {
                spaces -= rowsHints[r][i];
            }
            calculatePerms(r, 0, spaces, 0, 0, resolves);
            if (resolves.isEmpty()) {
                throw new RuntimeException("Could not found the solution for " + r + "th row.");
            }
            rowPermutations[r] = new long[resolves.size()];
            while (!resolves.isEmpty()) {
                rowPermutations[r][resolves.size() - 1] = resolves.pollLast();
            }
        }

        //계산
        columnValue = new int[ROW_SIZE][COLUMN_SIZE];
        columnIndex = new int[ROW_SIZE][COLUMN_SIZE];
        mask = new long[ROW_SIZE];
        val = new long[ROW_SIZE];
        if (dfs(0)) {
            for (int r = 0; r < ROW_SIZE; r++) {
                for (int c = 0; c < COLUMN_SIZE; c++) {
                    out.print((grid[r] & (1L << c)) == 0 ? EMPTY : FILLED);
                }
                out.printLine();
            }
        } else {
            out.printLine("Sorry. I can't not solve the answer");
        }
        out.close();
    }

    private boolean dfs(int row) {
        if (row == ROW_SIZE) {
            return true;
        }
        rowMask(row); //다음 행에서 유효한 마스크 계산
        for (int i = 0; i < rowPermutations[row].length; i++) {
            if ((rowPermutations[row][i] & mask[row]) != val[row]) {
                continue;
            }
            grid[row] = rowPermutations[row][i];
            updateColumns(row);
            if (dfs(row + 1)) {
                return true;
            }
        }
        return false;
    }

    private void rowMask(int row) {
        mask[row] = val[row] = 0;
        if (row == 0) {
            return;
        }
        long ixc = 1L;
        for (int c = 0; c < COLUMN_SIZE; c++, ixc <<= 1) {
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

    private void updateColumns(int row) {
        long indexColumn = 1L;
        for (int c = 0; c < COLUMN_SIZE; c++, indexColumn <<= 1) {
            // 이전 값을 복사한다
            columnValue[row][c] = row == 0 ? 0 : columnValue[row - 1][c];
            columnIndex[row][c] = row == 0 ? 0 : columnIndex[row - 1][c];
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

    /**
     * System Console Output Purpose
     */
    class OutputWriter {
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
