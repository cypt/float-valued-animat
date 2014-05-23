package ru.nsu.alife.core.logic.impl.history;

import org.apache.log4j.Level;
import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.fs.IActionsProvider;
import ru.nsu.alife.core.logic.fs.IGoal;
import ru.nsu.alife.core.logic.impl.statistics.CubeUtils;
import ru.nsu.alife.core.logic.impl.statistics.StatisticCubesHolder;

/*
 * History Should be that current element descripts some state in the time T and action that was selected at previous
  * of T step
 */
public class HistoryUtils {
    private HistoryUtils() {
        throw new UnsupportedOperationException("HistoryUtils() is private!");
    }

    public static void initialFillStatisticCube(final StatisticCubesHolder statisticCubesHolder,
                                                final HistoryHolder historyHolder,
                                                final IGoal goal) {
        if (historyHolder.size() == 0) {
            return;
        }

        HistoryElement currentHistoryElement = historyHolder.getLastElement();
        HistoryElement previousHistoryElement = currentHistoryElement;

        int currentIndex = historyHolder.getLastIndex();
        long lastEndOfActionTimestamp = previousHistoryElement.timestamp;
        int lastAction = currentHistoryElement.activeActionIndex;

        boolean setSuccess = false;

        while (currentIndex >= 1) {
            previousHistoryElement = currentHistoryElement;
            currentHistoryElement = historyHolder.get(currentIndex--);

            if (currentHistoryElement == previousHistoryElement) {
                continue;
            }

            if (previousHistoryElement.endOfAction) {
                lastEndOfActionTimestamp = previousHistoryElement.timestamp;
                lastAction = previousHistoryElement.activeActionIndex;
                setSuccess = goal.check(previousHistoryElement.sensorsValues);
            }

            final long deltaTime = lastEndOfActionTimestamp - currentHistoryElement.timestamp;
            final float[] sensorValues = currentHistoryElement.sensorsValues.clone();
            final int durationIndex = CubeUtils.getIndexFromDurationValue(deltaTime, lastAction);

            statisticCubesHolder
                    .updateStatistic(sensorValues, lastAction, durationIndex, setSuccess);
        }

        flushLearn(statisticCubesHolder);
    }

    public static void fillStatisticCube(final StatisticCubesHolder statisticCubesHolder,
                                         final HistoryHolder historyHolder, final IGoal goal) {

        if (historyHolder.size() == 0) {
            return;
        }

        HistoryElement currentHistoryElement = historyHolder.getLastElement();
        HistoryElement previousHistoryElement = currentHistoryElement;

        int currentIndex = historyHolder.getLastIndex();
        long lastEndOfActionTimestamp = previousHistoryElement.timestamp;
        int lastAction = currentHistoryElement.activeActionIndex;
        //System.out.println(lastAction);
        boolean setSuccess = false;

        while (currentIndex >= 1) {
            previousHistoryElement = currentHistoryElement;
            currentHistoryElement = historyHolder.get(currentIndex--);

            if (currentHistoryElement == previousHistoryElement) {
                continue;
            }

            if (previousHistoryElement.endOfAction) {
                lastEndOfActionTimestamp = previousHistoryElement.timestamp;
                lastAction = previousHistoryElement.activeActionIndex;
                setSuccess = goal.check(previousHistoryElement.sensorsValues);
            }


            final long deltaTime = lastEndOfActionTimestamp - currentHistoryElement.timestamp;
            final float[] sensorValues = currentHistoryElement.sensorsValues.clone();
            final int durationIndex = CubeUtils.getIndexFromDurationValue(deltaTime, lastAction);

            statisticCubesHolder
                    .updateStatistic(sensorValues, lastAction, durationIndex, setSuccess);

            //System.out.println("Updated action " + lastAction + " with duration " + deltaTime + " and success " + setSuccess);
            if (currentHistoryElement.endOfAction == true) {
                break;
            }
        }
        backLearning(statisticCubesHolder, historyHolder, goal, previousHistoryElement, currentIndex++);
    }

    private static void backLearning (final StatisticCubesHolder statisticCubesHolder,
                                      final HistoryHolder historyHolder, final IGoal goal, final HistoryElement lastElem,
                                      int lastCount) {
        HistoryElement currElem;
        while(true) {
            currElem =  historyHolder.get(lastCount++);
            boolean setSuccess = goal.check(currElem.sensorsValues);
            final long deltaTime =  currElem.timestamp - lastElem.timestamp;
            final int durationIndex = CubeUtils.getIndexFromDurationValue(deltaTime, lastElem.activeActionIndex);
            statisticCubesHolder
                    .updateStatistic(lastElem.sensorsValues, lastElem.activeActionIndex,durationIndex , setSuccess);
            if (currElem.endOfAction == true) {
                break;
            }

        }

    }
    public static void flushLearn(final StatisticCubesHolder statisticCubesHolder) {
        statisticCubesHolder.endUpdatingBatch();
    }
}
