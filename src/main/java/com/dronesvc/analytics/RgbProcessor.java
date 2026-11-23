package com.dronesvc.analytics;

import java.util.ArrayList;
import java.util.List;

public class RgbProcessor implements DataProcessor {

    private static final double MIN_SHARPNESS = 0.7;

    @Override
    public String name() {
        return "RGB image quality check";
    }

    @Override
    public String sensorType() {
        return SensorData.RGB;
    }

    @Override
    public List<Finding> process(SensorData data) {
        List<Finding> findings = new ArrayList<>();
        for (SensorData.Sample sample : data.getSamples()) {
            if (sample.value() < MIN_SHARPNESS) {
                findings.add(new Finding(sample.point(),
                        String.format("%s is blurred (sharpness %.2f), area needs a re-shoot", sample.file(), sample.value())));
            }
        }
        return findings;
    }
}
