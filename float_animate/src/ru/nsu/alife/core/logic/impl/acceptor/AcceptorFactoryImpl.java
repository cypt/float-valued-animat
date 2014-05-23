package ru.nsu.alife.core.logic.impl.acceptor;

import ru.nsu.alife.core.logic.fs.*;
import ru.nsu.alife.core.logic.impl.history.HistoryHolder;

import java.util.ArrayList;
import java.util.List;

public class AcceptorFactoryImpl implements IAcceptorFactory {
    private final List<IUpdateStatisticListener> updateStatisticListeners = new ArrayList<IUpdateStatisticListener>();

    private final ISensorsProvider sensorsProvider;
    private final IActionsProvider actionsProvider;
    private final HistoryHolder historyHolder;

    public AcceptorFactoryImpl(final ISensorsProvider sensorsProvider, final IActionsProvider actionsProvider,
                               final HistoryHolder historyHolder) {
        this.sensorsProvider = sensorsProvider;
        this.actionsProvider = actionsProvider;
        this.historyHolder = historyHolder;
    }

    @Override
    public IAcceptor getAcceptor(final IGoal goal) {
        final AcceptorImpl acceptor = new AcceptorImpl(goal, actionsProvider, sensorsProvider, historyHolder);
        for (final IUpdateStatisticListener updateStatisticListener : updateStatisticListeners) {
            acceptor.addUpdateStatisticListener(updateStatisticListener);
        }
        return acceptor;
    }
}
