package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.impl.acceptor.AcceptorImpl;
import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.StatisticCubesHolder;
import ru.nsu.alife.core.logic.impl.statistics.VolumeGoalImpl;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;

public class CompositeRule implements IRule {

    private final VolumeGoalImpl volumeGoal;
    private final AcceptorImpl acceptorImpl;

    public CompositeRule(
            final VolumeGoalImpl volumeGoal, final AcceptorImpl acceptorImpl) {
        this.volumeGoal = volumeGoal;
        this.acceptorImpl = acceptorImpl;
    }

    @Override
    public IVolume getSituationDescription() {
        return volumeGoal.getVolume();
    }

    @Override
    public double getProbability() {
        return volumeGoal.getStatistic().getProbability();
    }

    @Override
    public boolean execute(final IAcceptor acceptor) {
        final float[] sensorValues = Utils.getSensorValues(acceptorImpl.getSensorsProvider());

        final boolean check = volumeGoal.check(sensorValues);

        if (check) {
            final IRule rule = acceptorImpl.getRule(sensorValues);
            if (rule != null) {
                return rule.execute(acceptorImpl);
            }
        }

        return false;
    }

    @Override
    public boolean equals(final Object o) {
        return RuleComparator.compare(this, (Rule) o);
    }

    @Override
    public int hashCode() {
        return getSituationDescription().hashCode();
    }

}


