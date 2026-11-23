package com.dronesvc.analytics;

import java.util.ArrayList;
import java.util.List;

public class ThermalProcessor implements DataProcessor {

    private static final double HOT_SPOT_CELSIUS = 60.0;

    @Override
    public String name() {
        return "thermal hot-spot detection";
    }

    @Override
    public String sensorType() {
        return SensorData.THERMAL;
    }

    @Override
    public List<Finding> process(SensorData data) {
        List<Finding> findings = new ArrayList<>();
        for (SensorData.Sample sample : data.getSamples()) {
            if (sample.value() > HOT_SPOT_CELSIUS) {
                findings.add(new Finding(sample.point(),
                        String.format("hot spot %.1f C in %s, possible faulty panel", sample.value(), sample.file())));
            }
        }
        return findings;
    }
}
