package ru.nsu.alife.core.logic.impl.history;

import ru.nsu.alife.core.LoggerHolder;

import java.io.*;
import java.util.ArrayList;

public class HistoryHolder {

    private final static int INITIAL_SIZE = 1000;
    private final ArrayList<HistoryElement> historyElements = new ArrayList<HistoryElement>(
            INITIAL_SIZE);

    private HistoryCache cache;
    private boolean cacheOnly;

    public void beginSaving(final String fileName, boolean cacheOnly) {
        stopSaving();
        this.cacheOnly = cacheOnly;
        cache = new HistoryCache(fileName, true);
        cache.initOutput();
    }

    public void stopSaving() {
        if (cache != null) {
            cache.closeOutput();
        }
    }

    public boolean isSaving() {
        return cache != null;
    }

    public boolean add(HistoryElement historyElement) {
        if (cache != null) {
            cache.saveHistory(historyElement);
        }
        if (!cacheOnly) {
            return historyElements.add(historyElement);
        }
        return true;
    }

    public HistoryElement getLastElement() {
        if (historyElements.size() == 0) {
            return null;
        } else {
            return historyElements.get(historyElements.size() - 1);
        }
    }

    public int getLastIndex() {
        return (historyElements.size() - 1);
    }

    public int size() {
        return historyElements.size();
    }

    public HistoryElement get(int index) {
        return historyElements.get(index);
    }

    public static HistoryHolder restoreHistoryHolder(final String fileName) {
        final File file = new File(fileName);

        if (!file.exists() || !file.isFile()) {
            return null;
        }

        LoggerHolder.logger.debug("Begin restoring history");

        try {
            HistoryHolder historyHolder = new HistoryHolder();
            DataInputStream historyInput = new DataInputStream(new FileInputStream(file));

            try {

                while (true) {

                    try {
                        HistoryElement historyElement = new HistoryElement();
                        historyElement.timestamp = historyInput.readLong();
                        historyElement.activeActionIndex = historyInput.readInt();
                        historyElement.sensorsValues = new float[historyInput.readInt()];

                        for (int i = 0; i < historyElement.sensorsValues.length; i++) {
                            historyElement.sensorsValues[i] = historyInput.readFloat();
                        }

                        historyElement.endOfAction = historyInput.readBoolean();
                        historyHolder.add(historyElement);
                        LoggerHolder.logger.debug("restored " + historyElement.getTimestamp());
                    } catch (EOFException e) {
                        LoggerHolder.logger.error(e.getMessage(), e);
                        break;
                    }
                }

                return historyHolder;

            } finally {
                historyInput.close();
            }

        } catch (IOException e) {
            LoggerHolder.logger.error(e.getMessage(), e);
        } finally {
            LoggerHolder.logger.debug("finish restoring history");
        }

        return null;
    }
}
