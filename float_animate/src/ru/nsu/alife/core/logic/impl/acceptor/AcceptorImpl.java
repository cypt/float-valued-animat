package ru.nsu.alife.core.logic.impl.acceptor;

import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.fs.*;
import ru.nsu.alife.core.logic.impl.history.HistoryHolder;
import ru.nsu.alife.core.logic.impl.history.HistoryUtils;
import ru.nsu.alife.core.logic.impl.statistics.CubeUtils;
import ru.nsu.alife.core.logic.impl.statistics.StatisticCubesHolder;
import ru.nsu.alife.core.logic.impl.statistics.VolumeGoalImpl;
import ru.nsu.alife.core.logic.impl.statistics.goal_finding_algorithms.FindGoodVolumeAlgorithm;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.float_world_impl.Experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AcceptorImpl implements IAcceptor {

    private final IActionsProvider actionsProvider;
    private final ISensorsProvider sensorsProvider;
    private final HistoryHolder historyHolder;
    private final StatisticCubesHolder statisticCubesHolder;

    private final IGoal goal;

    private boolean goalReached;

    private final List<AcceptorListener> acceptorListeners = new ArrayList<AcceptorListener>();

    private int lCounter = 0;

    public AcceptorImpl(
            final IGoal goal, final IActionsProvider actionsProvider, final ISensorsProvider sensorsProvider,
            final HistoryHolder historyHolder) {
        this.goal = goal;
        this.actionsProvider = actionsProvider;
        this.sensorsProvider = sensorsProvider;
        this.historyHolder = historyHolder;
        this.statisticCubesHolder = new StatisticCubesHolder(Experiment.ACTION_DIMENSION_SIZES);
    }

    @Override
    public void launchLearnOnHistory() {
        HistoryUtils.initialFillStatisticCube(statisticCubesHolder, historyHolder, goal);
        //        if (goal instanceof AnimalSatisfiedGoal) {
        //            SerializeUtils.serializeExtendedStatisticCubes(String.valueOf(System.currentTimeMillis()),
        //                    statisticCubesHolder.getExtendedStatisticCubes());
        //        }
    }

    @Override
    public void learn() {
        lCounter++;
        HistoryUtils.fillStatisticCube(statisticCubesHolder, historyHolder, goal);
        if (lCounter > 10) {

            lCounter = 0;

            HistoryUtils.flushLearn(statisticCubesHolder);
        }
    }

    @Override
    public void update(final long currentTimestamp) {
        goalReached = goal.check();
        LoggerHolder.logger.log(org.apache.log4j.Level.DEBUG, "goalReached = " + goal + " is " + goalReached);
    }

    @Override
    public void invokeFs(final boolean executor) {
        LoggerHolder.logger.log(org.apache.log4j.Level.DEBUG, "goal reached");
        for (AcceptorListener acceptorListener : acceptorListeners) {
            acceptorListener.onResult(goalReached, executor);
        }
    }

    @Override
    public void performAction(final IAction action, final long time) {
        actionsProvider.performAction(this, action, time);
    }

    public IActionsProvider getActionsProvider() {
        return actionsProvider;
    }

    public ISensorsProvider getSensorsProvider() {
        return sensorsProvider;
    }

    public StatisticCubesHolder getStatisticCubesHolder() {
        return statisticCubesHolder;
    }

    @Override
    public HashMap<IRule, IGoal> generateGoals() {
        LoggerHolder.logger.debug("generateGoals");

        final HashMap<IRule, IGoal> result = new HashMap<IRule, IGoal>();

        List<? extends IVolume> goodVolumes = new FindGoodVolumeAlgorithm(
                statisticCubesHolder.getSlicedStatisticCubes()).start();

        for (final IVolume volume : goodVolumes) {
            final VolumeGoalImpl goal = new VolumeGoalImpl(getSensorsProvider(), volume);
            LoggerHolder.log(goal);
            result.put(new CompositeRule(goal, this), goal);
        }

        return result;
    }

    public IRule getRule(final float[] sensorValues) {
        final StatisticCubesHolder.Result ruleData = statisticCubesHolder.getRule(sensorValues);

        if (ruleData == null) {
            return null;
        } else {
            return new Rule(ruleData.probability, getAction(ruleData.actionIndex), null,
                    CubeUtils.getDurationFromIndexValue(ruleData.durationIndex, ruleData.actionIndex));
        }
    }

    public void addUpdateStatisticListener(final IUpdateStatisticListener updateStatisticListener) {
        statisticCubesHolder.addUpdateStatisticListener(updateStatisticListener);
    }

    public void addAcceptorListener(final AcceptorListener element) {
        acceptorListeners.add(element);
    }

    public IAction getAction(final int index) {
        return actionsProvider.getActions().get(index);
    }
}
