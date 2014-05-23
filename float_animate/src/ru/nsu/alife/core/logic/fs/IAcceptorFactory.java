package ru.nsu.alife.core.logic.fs;

public interface IAcceptorFactory {
    public IAcceptor getAcceptor(final IGoal goal);
}
