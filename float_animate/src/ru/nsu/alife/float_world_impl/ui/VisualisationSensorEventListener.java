package ru.nsu.alife.float_world_impl.ui;

import ru.nsu.alife.core.sensors.ISensorEventListener;
import ru.nsu.alife.float_world_impl.sensors.DistanceSensor;
import ru.nsu.alife.float_world_impl.sensors.AngleSensor;

import javax.swing.*;
import java.awt.*;

public class VisualisationSensorEventListener implements ISensorEventListener {
    final JLabel outputValueLabel1;
    final JLabel outputValueLabel2;

    public VisualisationSensorEventListener(final JLabel outputValueLabel1, final JLabel outputValueLabel2) {
        this.outputValueLabel1 = outputValueLabel1;
        this.outputValueLabel2 = outputValueLabel2;
    }

    @Override
    public final void onSensorEvent(final SensorEvent sensorEvent) {
        final String type = sensorEvent.getEventType();
        if (DistanceSensor.AppleAtFrontEvent.APPLE_AT_FRONT_DETECTION_EVENT.equals(type)) {
            final DistanceSensor.AppleAtFrontEvent appleAtFrontEvent = (DistanceSensor.AppleAtFrontEvent)
                    sensorEvent;
            if (appleAtFrontEvent.isDetected()) {
                appleAtFrontEvent.getApple().setColor(Color.BLACK);
            }
            outputValueLabel1.setText(
                    "sensor " + appleAtFrontEvent.getSensor().getName() + " value = " + appleAtFrontEvent.getSensor()
                            .getValue());
        } else if (AngleSensor.SideApplesDetectionEvent.SIDE_APPLES_DETECTION_EVENT.equals(type)) {
            final AngleSensor.SideApplesDetectionEvent appleAtSideEvent = (AngleSensor
                    .SideApplesDetectionEvent) sensorEvent;
            if (appleAtSideEvent.isDetected()) {
                if (appleAtSideEvent.getRotationShift() != 0) {
                    appleAtSideEvent.getApple().setColor(Color.YELLOW);
                }
            }
            outputValueLabel2.setText(
                    "sensor " + appleAtSideEvent.getSensor().getName() + " value = " + appleAtSideEvent.getSensor()
                            .getValue());
        }
    }
}
