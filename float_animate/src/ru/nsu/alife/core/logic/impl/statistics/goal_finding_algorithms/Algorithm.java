package ru.nsu.alife.core.logic.impl.statistics.goal_finding_algorithms;

import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;

import java.util.List;

public interface Algorithm {
    public List<? extends IVolume> start();
}
