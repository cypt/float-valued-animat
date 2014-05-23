package ru.nsu.alife.core.logic.fs;

import java.util.HashMap;

public interface IAcceptor {
    public static final String TAG = IAcceptor.class.getName();

    public void performAction(IAction action, long time);

    public void invokeFs(final boolean actionStopped);

    public void update(long currentTime);

    public void addAcceptorListener(final AcceptorListener element);

    void launchLearnOnHistory();

    HashMap<IRule, IGoal> generateGoals();

    void learn();
}