package ru.nsu.alife.float_world_impl.ui;

import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.StatisticCube;

import javax.swing.*;

public class CubeSlicePanel extends JPanel {
    private static final int CELL_WIDTH = 100;
    private static final int CELL_HEIGHT = 20;
    private static final int MARGINS = 5;
    private final int rows;
    private final int columns;
    private JLabel[][] labels;

    public CubeSlicePanel(final int rows, final int columns) {
        this.setSize(columns * CELL_WIDTH + (columns + 1) * MARGINS, rows * CELL_HEIGHT + (rows + 1) * MARGINS);
        this.setLayout(null);
        this.rows = rows;
        this.columns = columns;
        int labelY = MARGINS;
        labels = new JLabel[rows][columns];
        for (int i = 0; i < rows; i++) {
            int labelX = MARGINS;
            for (int j = 0; j < columns; j++) {
                final JLabel label = new JLabel();
                labels[i][j] = label;
                label.setLocation(labelX, labelY);
                label.setSize(CELL_WIDTH, CELL_HEIGHT);
                this.add(label);
                labelX += CELL_WIDTH + MARGINS;
            }
            labelY += CELL_HEIGHT + MARGINS;
        }
    }

    public void setValue(final int row, final int column, final StatisticCube.SimpleStatisticCell cell) {
        final JLabel label = labels[row][column];
        labels[row][column].setText(cell == null ? "[]" : cell.toString());
    }

    public void setValue(final int row, final int column, final ExtendedStatisticCube.ExtendedStatisticCell cell) {
        final JLabel label = labels[row][column];
        labels[row][column].setText(cell == null ? "[]" : cell.toString());
    }

    public void setValue(final int row, final int column, final Object cell) {
        final JLabel label = labels[row][column];
        labels[row][column].setText(cell == null ? "[]" : cell.toString());
    }
}
