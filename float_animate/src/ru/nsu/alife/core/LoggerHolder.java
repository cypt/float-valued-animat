package ru.nsu.alife.core;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import ru.nsu.alife.core.logic.fs.IAction;
import ru.nsu.alife.core.logic.fs.Plan;
import ru.nsu.alife.core.logic.fs.Rule;
import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.core.logic.impl.statistics.volumes.MergedSubVolume;
import ru.nsu.alife.core.logic.impl.statistics.volumes.VolumeCell;
import ru.nsu.alife.core.sensors.ISensor;
import ucar.ma2.ArrayBoolean;
import ucar.ma2.ArrayByte;
import ucar.ma2.IndexIterator;

import java.util.Arrays;
import java.util.List;

public class LoggerHolder {
    public static final Logger logger = Logger.getLogger("ru.nsu.alife.core");

    static {
        BasicConfigurator.configure();
        logger.setLevel(Level.ALL);
    }

    public static void logRules(final ExtendedStatisticCube extendedStatisticCube) {
        IndexIterator indexIterator = extendedStatisticCube.getCube().getIndexIterator();
        while (indexIterator.hasNext()) {
            indexIterator.next();
            final ExtendedStatisticCube.ExtendedStatisticCell cell = (ExtendedStatisticCube.ExtendedStatisticCell)
                    indexIterator
                    .getObjectCurrent();
            if (cell != null && cell.totalResults > 0 && cell.getProbability() > 0.8) {
                LoggerHolder.logger.debug(Arrays.toString(indexIterator
                        .getCurrentCounter()) + " : pos " + cell.positiveResults + " tot " + cell.totalResults + " " +
                        "dur " + cell.durationIndex);
            }
        }
        return;
    }

    public static void log(final ExtendedStatisticCube extendedStatisticCube) {
        final int[] dimensionSizes = extendedStatisticCube.getDimensionSizesWithProjectionsIncluded();

        if (dimensionSizes.length != 2) {
            IndexIterator indexIterator = extendedStatisticCube.getCube().getIndexIterator();
            while (indexIterator.hasNext()) {
                indexIterator.next();
                final ExtendedStatisticCube.ExtendedStatisticCell cell = (ExtendedStatisticCube
                        .ExtendedStatisticCell) indexIterator
                        .getObjectCurrent();
                if (cell != null && cell.totalResults > 0) {
                    LoggerHolder.logger.debug(Arrays.toString(indexIterator
                            .getCurrentCounter()) + " : pos " + cell.positiveResults + " tot " + cell.totalResults +
                            " dur " + cell.durationIndex);
                }
            }
            return;
        }

        for (int i = 0; i < dimensionSizes[0]; i++) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < dimensionSizes[1]; j++) {
                final int[] index = new int[]{i, j};
                final ExtendedStatisticCube.ExtendedStatisticCell extendedStatisticCell = extendedStatisticCube
                        .getAt(index);
                stringBuilder.append(extendedStatisticCell);
                stringBuilder.append(" ");
            }
            logger.log(Level.DEBUG, stringBuilder.toString());
        }
    }

    public static void log(final ArrayBoolean arrayBoolean) {
        final int[] dimensionSizes = arrayBoolean.getShape();

        if (dimensionSizes.length != 2) {
            IndexIterator indexIterator = arrayBoolean.getIndexIterator();
            while (indexIterator.hasNext()) {
                indexIterator.next();
                LoggerHolder.logger.debug(Arrays.toString(indexIterator.getCurrentCounter()) + " : " + indexIterator
                        .getBooleanCurrent());
            }
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dimensionSizes[0]; i++) {
            for (int j = 0; j < dimensionSizes[1]; j++) {
                final int[] index = new int[]{i, j};
                char symbol = arrayBoolean.get(arrayBoolean.getIndex().set(index)) ? '♦' : '◊';
                stringBuilder.append(symbol);
                //stringBuilder.append("\t");
            }
            stringBuilder.append("\n");
        }
        logger.log(Level.DEBUG, stringBuilder.toString());
    }

    public static void log(final List<ISensor> sensors, final List<IAction> actions) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (ISensor sensor : sensors) {
            stringBuilder.append(sensor.getName() + " : " + sensor.getValue() + " ");
        }

        for (IAction action : actions) {
            if (action.isActive()) {
                stringBuilder.append(action.getName() + " ");
            }
        }
        logger.log(Level.DEBUG, stringBuilder.toString());
    }

    private LoggerHolder() {
        throw new UnsupportedOperationException();
    }

    public static void log(final IVolume volume) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (volume instanceof MergedSubVolume) {
            final MergedSubVolume mergedSubVolume = (MergedSubVolume) volume;
            final ExtendedStatisticCube[] extendedStatisticCubes = mergedSubVolume.getExtendedStatisticCubes();

            final ArrayByte cube = mergedSubVolume.getCube();
            final IndexIterator indexIterator = cube.getIndexIterator();

            while (indexIterator.hasNext()) {

                indexIterator.next();
                final byte byteCurrent = indexIterator.getByteCurrent();

                if (byteCurrent != -1) {
                    final ExtendedStatisticCube.ExtendedStatisticCell at = extendedStatisticCubes[byteCurrent]
                            .getAt(indexIterator.getCurrentCounter());
                    LoggerHolder.logger.debug(Arrays.toString(indexIterator
                            .getCurrentCounter()) + " : " + byteCurrent + " : pos " + at.positiveResults + " tot " +
                            at.totalResults + " prob " + at
                            .getProbability());
                }
            }
        } else if (volume instanceof VolumeCell) {
            VolumeCell volumeCell = (VolumeCell) volume;
            logger.debug(Arrays.toString(volumeCell.getPoint()));
        }
    }

    public static void log(final Rule rule) {
        logger.debug("Rule :");
        logger.debug(rule.getSituationDescription());
        logger.debug(rule.getAction());
    }

    public static void log(final Plan selectedPlan) {
        if (selectedPlan == null) {
            LoggerHolder.logger.debug("NO PLAN");
        } else {
            LoggerHolder.logger.debug("Plan : \n");
            LoggerHolder.logger.debug("Rule : " + selectedPlan.getRule());
            LoggerHolder.logger.debug("SubFs : " + selectedPlan.getFs());
        }
    }
}
