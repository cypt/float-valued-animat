package ru.nsu.alife.core.logic.impl.statistics.volumes;

public interface IVolume {

    public static class VolumeStatistic implements Cloneable{
        long positiveResults;
        long totalResults;

        public double getProbability() {
            return (double) positiveResults / totalResults;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            VolumeStatistic volumeStatistic = new VolumeStatistic();
            volumeStatistic.positiveResults = positiveResults;
            volumeStatistic.totalResults = totalResults;
            return volumeStatistic;
        }
    }

    public boolean isInside(final float[] point);

    public boolean compareTo(IVolume volume);

    public VolumeStatistic getStatistic();

}
