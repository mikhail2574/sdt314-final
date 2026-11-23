package com.dronesvc.analytics;

public class RgbPipeline extends DataPipeline {

    @Override
    protected DataProcessor createProcessor() {
        return new RgbProcessor();
    }
}
