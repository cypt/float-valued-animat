package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.fs.IAcceptor;
import ru.nsu.alife.core.logic.fs.IAction;

import java.util.ArrayList;

public interface IActionsProvider {
    public ArrayList<IAction> getActions();

    public void performAction(final IAcceptor executor, final IAction action, final long time);

    public IAction getCurrentAction();
}
