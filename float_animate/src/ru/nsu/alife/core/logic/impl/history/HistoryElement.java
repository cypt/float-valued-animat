package ru.nsu.alife.core.logic.impl.history;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class HistoryElement implements Externalizable {

    private static final long serialVersionUID = -3989169590424047874l;

    public long timestamp;
    public float[] sensorsValues;
    public int activeActionIndex;
    public boolean endOfAction;

    public HistoryElement() {
    }

    public HistoryElement(final long timestamp, final float[] sensorsValues, final int activeActionIndex,
                          final boolean endOfAction) {
        this.timestamp = timestamp;
        this.sensorsValues = sensorsValues;
        this.activeActionIndex = activeActionIndex;
        this.endOfAction = endOfAction;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeLong(timestamp);
        out.writeObject(sensorsValues);
        out.writeInt(activeActionIndex);
        out.writeBoolean(endOfAction);
        out.flush();
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        timestamp = in.readLong();
        sensorsValues = (float[])in.readObject();
        activeActionIndex = in.readInt();
        endOfAction = in.readBoolean();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public float[] getSensorsValues() {
        return sensorsValues;
    }

    public void setSensorsValues(final float[] sensorsValues) {
        this.sensorsValues = sensorsValues;
    }

    public int getActiveActionIndex() {
        return activeActionIndex;
    }

    public void setActiveActionIndex(final int activeActionIndex) {
        this.activeActionIndex = activeActionIndex;
    }

    public boolean isEndOfAction() {
        return endOfAction;
    }

    public void setEndOfAction(final boolean endOfAction) {
        this.endOfAction = endOfAction;
    }

    @Override
    public String toString() {
        return "HistoryElement{" +
                "timestamp=" + timestamp +
                ", sensorsValues=" + Arrays.toString(sensorsValues) +
                ", activeActionIndex=" + activeActionIndex +
                ", endOfAction=" + endOfAction +
                '}';
    }
}
