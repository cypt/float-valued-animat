package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.sensors.ISensor;

import java.util.ArrayList;

public interface ISensorsProvider {
    public ArrayList<ISensor> getSensors();
}
