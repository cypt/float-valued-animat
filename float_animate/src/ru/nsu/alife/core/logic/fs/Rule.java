package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;

public class Rule implements IRule {

    private double probability;
    private IVolume situationDescription;
    private IAction action;
    private long time;

    public Rule() {
    }

    public Rule(final double probability, final IAction action, final IVolume situationDescription, final long time) {
        this.probability = probability;
        this.action = action;
        this.situationDescription = situationDescription;
        this.time = time;
    }

    public IAction getAction() {
        return action;
    }

    @Override
    public IVolume getSituationDescription() {
        return situationDescription;
    }

    public void setAction(final IAction action) {
        this.action = action;
    }

    public void setSituationDescription(final IVolume situationDescription) {
        this.situationDescription = situationDescription;
    }

    @Override
    public boolean execute(IAcceptor acceptor) {
        acceptor.performAction(action, time);
        //System.out.println("Action " + action + " with time " + time);

        return true;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    @Override
    public boolean equals(final Object o) {
        if (o.getClass() != Rule.class) {
            return false;
        }

        RuleComparator.compare(this, (Rule)o);

        return true;
    }

    @Override
    public int hashCode() {
        int result = situationDescription != null ? situationDescription.hashCode() : 0;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
