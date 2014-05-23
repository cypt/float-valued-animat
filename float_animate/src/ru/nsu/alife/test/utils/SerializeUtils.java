package ru.nsu.alife.test.utils;

import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;

import java.io.*;

public class SerializeUtils {

    public static final String EXTENDED_STATISTIC_CUBE_STORAGE = "cubes";

    public static void serializeExtendedStatisticCubes(String prefix, ExtendedStatisticCube[] extendedStatisticCubes
    ) {
        try {
            final ObjectOutputStream oOut = new ObjectOutputStream(new FileOutputStream(prefix +
                    EXTENDED_STATISTIC_CUBE_STORAGE));

            oOut.writeObject(extendedStatisticCubes);

            oOut.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ExtendedStatisticCube[] deserializeExtendedStatisticCubes() {
        try {
            final ObjectInputStream oInput = new ObjectInputStream(new FileInputStream(EXTENDED_STATISTIC_CUBE_STORAGE));
            return (ExtendedStatisticCube[]) oInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
