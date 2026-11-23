package com.dronesvc.analytics;

public class ThermalPipeline extends DataPipeline {

    @Override
    protected DataProcessor createProcessor() {
        return new ThermalProcessor();
    }
}
