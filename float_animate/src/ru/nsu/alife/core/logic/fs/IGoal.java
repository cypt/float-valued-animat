package ru.nsu.alife.core.logic.fs;

public interface IGoal {
    public boolean check();

    public boolean check(final float[] history);
}
