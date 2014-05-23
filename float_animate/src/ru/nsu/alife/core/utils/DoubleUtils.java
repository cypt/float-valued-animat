package ru.nsu.alife.core.utils;

public final class DoubleUtils {

    public static final double D_EPSILON = 0.00001;
    public static final double F_EPSILON = 0.0001;

    private DoubleUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static boolean equals(double d1, double d2) {
        if (d1 == d2) {
            return true;
        }
        if (d1 == Double.NaN || d2 == Double.NaN) {
            return false;
        }
        return (Math.abs(d1 - d2) < D_EPSILON);
    }

    public static boolean equals(float f1, float f2) {
        if (f1 == f2) {
            return true;
        }
        if (f1 == Float.NaN || f2 == Float.NaN) {
            return false;
        }
        return (Math.abs(f1 - f2) < F_EPSILON);
    }
}
