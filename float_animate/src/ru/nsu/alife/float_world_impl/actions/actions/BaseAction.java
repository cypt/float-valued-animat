package ru.nsu.alife.float_world_impl.actions.actions;

import org.apache.log4j.Level;
import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.fs.IAction;
import ru.nsu.alife.core.logic.fs.IActionEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAction implements IAction {

    private final List<IActionEventListener> actionEventListeners = new ArrayList<IActionEventListener>();

    private boolean isActive;

    @Override
    public boolean doAction() {
        LoggerHolder.logger.log(Level.DEBUG, getName() + " is " + " started ");
        if (!isActive) {
            isActive = true;
            for (final IActionEventListener actionEventListener : actionEventListeners) {
                actionEventListener.onActionEvent(new IActionEventListener.IActionEvent() {
                    @Override
                    public IAction getIAction() {
                        return BaseAction.this;
                    }

                    @Override
                    public String getEventType() {
                        return IActionEventListener.IActionEvent.TYPE_STARTED;
                    }
                });
            }
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        LoggerHolder.logger.log(Level.DEBUG, getName() + " is " + " stopped ");
        if (isActive) {
            isActive = false;
            for (final IActionEventListener actionEventListener : actionEventListeners) {
                actionEventListener.onActionEvent(new IActionEventListener.IActionEvent() {
                    @Override
                    public IAction getIAction() {
                        return BaseAction.this;
                    }

                    @Override
                    public String getEventType() {
                        return IActionEventListener.IActionEvent.TYPE_STOPED;
                    }
                });
            }
        }
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public final void addActionEventListener(final IActionEventListener actionEventListener) {
        actionEventListeners.add(actionEventListener);
    }

    protected List<IActionEventListener> getActionEventListeners() {
        return actionEventListeners;
    }
}
