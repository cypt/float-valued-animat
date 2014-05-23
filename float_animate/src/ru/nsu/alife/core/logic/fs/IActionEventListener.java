package ru.nsu.alife.core.logic.fs;

public interface IActionEventListener {
    public static interface IActionEvent {
        public static final String TYPE_STARTED = "started";
        public static final String TYPE_STOPED = "stoped";

        public IAction getIAction();

        public String getEventType();
    }

    public void onActionEvent(final IActionEvent actionEvent);
}
