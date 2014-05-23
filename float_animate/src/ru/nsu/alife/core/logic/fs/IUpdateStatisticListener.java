package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.impl.statistics.StatisticCubesHolder;

public interface IUpdateStatisticListener {
    public void onStatisticUpdated(final StatisticCubesHolder statisticCubesHolder);
}
