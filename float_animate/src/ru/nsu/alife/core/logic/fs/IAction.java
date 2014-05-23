package ru.nsu.alife.core.logic.fs;

/**
 * Interface introducing any action in system.
 */
public interface IAction  {

    public boolean doAction();

    public boolean isActive();

    public void stop();

    public void addActionEventListener(final IActionEventListener iActionEventListener);

    public String getName();
}
