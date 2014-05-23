package ru.nsu.alife.core.logic.impl.history;

import java.io.*;

public class HistoryCache {

    public static final String DEFAULT_HIS = "default.his";

    private String fileName;

    private DataOutputStream historyOutput;

    private final boolean rewrite;

    private long counter;

    public HistoryCache(final String fileName, final boolean rewrite) {
        this.fileName = fileName;
        this.rewrite = rewrite;
    }

    public void saveHistory(final HistoryElement historyElement) {
        if (historyOutput == null) {
            initOutput();
        }

        if (historyOutput != null) {
            try {

                historyOutput.writeLong(historyElement.timestamp);
                historyOutput.writeInt(historyElement.activeActionIndex);
                historyOutput.writeInt(historyElement.sensorsValues.length);
                for (int i = 0; i < historyElement.sensorsValues.length; i++) {
                    historyOutput.writeFloat(historyElement.sensorsValues[i]);
                }

                historyOutput.writeBoolean(historyElement.endOfAction);

                if (counter++ % 40000000l == 0) {
                    historyOutput.flush();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initOutput() {
        try {
            final File file = new File(fileName);
            if (file.exists() && rewrite) {
                file.delete();
                file.createNewFile();
            } else if (!file.exists()) {
                file.createNewFile();
            }
            historyOutput = new DataOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            historyOutput = null;
        }
    }

    public void closeOutput() {
        if (historyOutput != null) {
            try {
                historyOutput.close();
                historyOutput = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static HistoryCache createDefault() {
        return new HistoryCache(DEFAULT_HIS, true);
    }
}
