package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.core.logic.fs.IAcceptor;

public interface IRule {

    double getProbability();

    boolean execute(IAcceptor acceptor);

    IVolume getSituationDescription();
}
