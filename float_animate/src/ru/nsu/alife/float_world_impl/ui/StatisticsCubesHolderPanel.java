package ru.nsu.alife.float_world_impl.ui;

import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.fs.IActionsProvider;
import ru.nsu.alife.core.logic.fs.ISensorsProvider;
import ru.nsu.alife.core.logic.fs.IUpdateStatisticListener;
import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.StatisticCubesHolder;
import ru.nsu.alife.core.logic.fs.IAcceptor;
import ucar.ma2.ArrayObject;
import ucar.ma2.Index;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StatisticsCubesHolderPanel extends JPanel {
    private static final int DELTA_HEIGHT = 20;
    private static final int DELTA_WIDTH = 20;

    private final List<JLabel> sensorValuesLabels = new ArrayList<JLabel>();
    private final List<JTextField> sensorValuesInputs = new ArrayList<JTextField>();
    private final JLabel durationValueLabel = new JLabel();
    private final JTextField durationValueInput = new JTextField();
    private final JButton updateButton = new JButton();
    private final JLabel horizontalDimensionName = new JLabel();
    private final JLabel verticalDimensionName = new JLabel();
    private final JSpinner actionsSpinner = new JSpinner();
    private CubeSlicePanel slicePanel;

    private StatisticCubesHolder statisticCubesHolder;

    private IAcceptor acceptor;

    private int[] cluedSensorDimensions;
    private ArrayList<Integer> freeDimensions;
    private Integer durationDimensionValue;

    private final int actionsCount;
    private final int sensorsCount;
    private final int[] sensorsDimensionSizes;
    private final int[] actionDimensionSizes;
    private final String[] sensorsNames;
    private final String[] actionsNames;

    private int selectedAction = 0;

    {
        setLayout(null);
        updateButton.setSize(100, 30);
        updateButton.setText("update");
        actionsSpinner.setSize(100, 30);
        actionsSpinner.setLocation(DELTA_WIDTH, DELTA_HEIGHT);
        verticalDimensionName.setSize(100, 30);
        horizontalDimensionName.setSize(100, 30);
        verticalDimensionName.setLocation(100 + 100 + DELTA_WIDTH + DELTA_WIDTH, DELTA_HEIGHT + 30 + DELTA_HEIGHT);
        horizontalDimensionName.setLocation(100 + 100 + DELTA_WIDTH + DELTA_WIDTH + 100 + DELTA_WIDTH, DELTA_HEIGHT);
        verticalDimensionName.setText("name1");
        horizontalDimensionName.setText("name2");
    }

    private String[] getSensorsNames(final ISensorsProvider sensorsProvider) {
        final String[] sensorNames = new String[sensorsProvider.getSensors().size()];
        for (int i = 0; i < sensorNames.length; i++) {
            sensorNames[i] = sensorsProvider.getSensors().get(i).getName();
        }
        return sensorNames;
    }

    private String[] getActionsNames(final IActionsProvider actionsProvider) {
        final String[] actionNames = new String[actionsProvider.getActions().size()];
        for (int i = 0; i < actionNames.length; i++) {
            actionNames[i] = actionsProvider.getActions().get(i).getName();
        }
        return actionNames;
    }

    public StatisticsCubesHolderPanel(final IAcceptor acceptor, final StatisticCubesHolder statisticCubesHolder,
                                      final ISensorsProvider sensorsProvider, final IActionsProvider actionsProvider) {
        this.acceptor = acceptor;
        this.statisticCubesHolder = statisticCubesHolder;
        this.sensorsCount = statisticCubesHolder.getSensorsCount();
        this.actionsCount = statisticCubesHolder.getActionsCount();

        this.sensorsDimensionSizes = statisticCubesHolder.getSensorsDimensionSizes();
        this.actionDimensionSizes = statisticCubesHolder.getActionDimensionSizes();

        sensorsNames = getSensorsNames(sensorsProvider);
        actionsNames = getActionsNames(actionsProvider);

        class SpinnerElement {
            String name;
            int index;

            SpinnerElement(String name, int index) {
                this.name = name;
                this.index = index;
            }

            public String toString() {
                return name;
            }
        }

        final SpinnerElement[] spinnerElements = new SpinnerElement[actionsCount];
        for (int i = 0; i < actionsNames.length; i++) {
            spinnerElements[i] = new SpinnerElement(actionsNames[i], i);
        }

        actionsSpinner.setModel(new SpinnerListModel(spinnerElements));

        int labelX = DELTA_WIDTH;
        int labelY = DELTA_HEIGHT * 2 + 30;

        for (int i = 0; i < sensorsCount; i++) {
            final JLabel label = new JLabel();
            label.setText(sensorsNames[i] + " [0.." + this.sensorsDimensionSizes[i] + ")");
            label.setLocation(labelX, labelY);
            label.setSize(100, 30);

            sensorValuesLabels.add(label);

            final JTextField input = new JTextField("0");
            input.setSize(100, 30);
            input.setLocation(labelX + 100 + DELTA_WIDTH, labelY);
            sensorValuesInputs.add(input);

            add(label);
            add(input);

            labelY += DELTA_HEIGHT + 30;
        }

        durationValueLabel.setText("duration");
        durationValueLabel.setLocation(labelX, labelY);
        durationValueLabel.setSize(100, 30);
        durationValueInput.setLocation(labelX + 100 + DELTA_WIDTH, labelY);
        durationValueInput.setSize(100, 30);
        labelY += DELTA_HEIGHT + 30;
        add(durationValueLabel);
        add(durationValueInput);
        updateButton.setLocation(labelX, labelY + DELTA_HEIGHT);
        add(updateButton);
        add(actionsSpinner);
        add(verticalDimensionName);
        add(horizontalDimensionName);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final SpinnerElement spinnerElement = (SpinnerElement) StatisticsCubesHolderPanel.this.actionsSpinner
                        .getValue();
                final int actionIndex = StatisticsCubesHolderPanel.this.selectedAction = spinnerElement.index;
                final ExtendedStatisticCube extendedStatisticCube = statisticCubesHolder
                        .getExtendedStatisticCubes()[actionIndex];
                final int[] dimSizes = extendedStatisticCube.getDimensionSizesWithProjectionsIncluded();
                final ArrayList<Integer> freeDimenstions = new ArrayList<Integer>();
                int freeDimensionsCounter = 0;
                final int[] cluedDimensions = new int[sensorsCount];
                for (int i = 0; i < sensorsCount; i++) {
                    final JTextField textField = sensorValuesInputs.get(i);
                    try {
                        if (textField.getText().compareTo("") == 0) {
                            freeDimenstions.add(i);
                            freeDimensionsCounter++;
                            if (freeDimensionsCounter > 2) {
                                throw new IllegalArgumentException("dim > 2");
                            }
                        } else {
                            final int value = Integer.decode(textField.getText());
                            if (value >= 0 && value <= dimSizes[i]) {
                                cluedDimensions[i] = value;
                            } else {
                                throw new IllegalArgumentException();
                            }
                        }
                    } catch (NumberFormatException numberFormatException) {
                        numberFormatException.printStackTrace();
                        return;
                    } catch (IllegalArgumentException illegalArgumentException) {
                        illegalArgumentException.printStackTrace();
                        return;
                    }
                }
                if (durationValueInput.getText().compareTo("") == 0) {
                    durationDimensionValue = null;
                } else {
                    try {
                        final int value = Integer.decode(durationValueInput.getText());
                        if (value >= 0 && value <= actionDimensionSizes[actionIndex]) {
                            durationDimensionValue = value;
                        } else {
                            throw new IllegalArgumentException();
                        }
                    } catch (NumberFormatException numberFormatException) {
                        numberFormatException.printStackTrace();
                        return;
                    } catch (IllegalArgumentException illegalArgumentException) {
                        illegalArgumentException.printStackTrace();
                        return;
                    }
                }
                setupTable(cluedDimensions, freeDimenstions);
            }
        });
    }

    private void setupTable(final int[] cluedDimensions, final ArrayList<Integer> freeDimensions) {
        int rows = 0;
        int columns = 0;
        final int freeDimensionsSize = freeDimensions.size();
        this.freeDimensions = freeDimensions;
        this.cluedSensorDimensions = cluedDimensions;
        if (freeDimensionsSize == 0) {
            rows = 1;
            columns = 1;
            horizontalDimensionName.setText("");
            verticalDimensionName.setText("");
        } else if (freeDimensionsSize == 1) {
            rows = 1;
            columns = this.sensorsDimensionSizes[freeDimensions.get(0)];
            horizontalDimensionName.setText("");
            verticalDimensionName.setText(this.sensorsNames[freeDimensions.get(0)]);
        } else if (freeDimensionsSize == 2) {
            rows = this.sensorsDimensionSizes[freeDimensions.get(0)];
            columns = this.sensorsDimensionSizes[freeDimensions.get(1)];
            horizontalDimensionName.setText(this.sensorsNames[freeDimensions.get(1)]);
            verticalDimensionName.setText(this.sensorsNames[freeDimensions.get(0)]);
        }
        setupTable(rows, columns);
        refillTable();
    }

    private void setupTable(final int rows, final int columns) {
        LoggerHolder.logger.debug("setup slicePanel");
        if (slicePanel != null) {
            slicePanel.setVisible(false);
            remove(slicePanel);
        }
        slicePanel = new CubeSlicePanel(rows, columns);
        slicePanel.setLocation(100 + 100 + DELTA_WIDTH + 100, DELTA_HEIGHT + 30);
        add(slicePanel);
    }

    private void refillTable() {
        LoggerHolder.logger.debug("refill slicePanel 1 ");
        if (slicePanel == null) {
            return;
        }
        LoggerHolder.logger.debug("refill slicePanel 2 ");
        ArrayObject arrayObject = null;
        if (durationDimensionValue != null) {
            arrayObject = statisticCubesHolder.getStatisticCubes()[selectedAction].getCubes()[durationDimensionValue];
        } else {
            arrayObject = statisticCubesHolder.getExtendedStatisticCubes()[selectedAction].getCube();
        }
        final Index index = arrayObject.getIndex();
        final int freeDimensionsSize = freeDimensions.size();
        for (int i = 0; i < cluedSensorDimensions.length; i++) {
            index.setDim(i, cluedSensorDimensions[i]);
        }
        if (freeDimensionsSize == 0) {
            slicePanel.setValue(0, 0, arrayObject.getObject(index));
        } else if (freeDimensionsSize == 1) {
            final int freeDimension = this.freeDimensions.get(0);
            final int freeDimensionSize = this.sensorsDimensionSizes[freeDimension];
            for (int i = 0; i < freeDimensionSize; i++) {
                index.setDim(freeDimension, i);
                slicePanel.setValue(0, i, arrayObject.getObject(index));
            }
        } else if (freeDimensionsSize == 2) {
            final int freeDimension1 = this.freeDimensions.get(0);
            final int freeDimension2 = this.freeDimensions.get(1);
            final int freeDimensionSize1 = this.sensorsDimensionSizes[freeDimension1];
            final int freeDimensionSize2 = this.sensorsDimensionSizes[freeDimension2];
            for (int i = 0; i < freeDimensionSize1; i++) {
                for (int j = 0; j < freeDimensionSize2; j++) {
                    index.setDim(freeDimension1, i);
                    index.setDim(freeDimension2, j);
                    slicePanel.setValue(i, j, arrayObject.getObject(index));
                }
            }
        }
    }

    public IUpdateStatisticListener getUpdateStatisticListener() {
        return new IUpdateStatisticListener() {
            @Override
            public void onStatisticUpdated(final StatisticCubesHolder statisticCubesHolder) {
                if (StatisticsCubesHolderPanel.this.statisticCubesHolder.equals(statisticCubesHolder)) {
                    StatisticsCubesHolderPanel.this.refillTable();
                }
            }
        };
    }
}
