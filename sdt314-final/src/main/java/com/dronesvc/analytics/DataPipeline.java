package com.dronesvc.analytics;

import java.util.List;

public abstract class DataPipeline {

    protected abstract DataProcessor createProcessor();

    public List<Finding> run(SensorData data) {
        DataProcessor processor = createProcessor();
        if (!processor.sensorType().equals(data.getSensorType())) {
            throw new IllegalArgumentException(processor.name() + " cannot read " + data.getSensorType() + " data");
        }
        List<Finding> findings = processor.process(data);
        System.out.printf("  %s: %d files checked, %d finding(s)%n", processor.name(), data.size(), findings.size());
        return findings;
    }
}
