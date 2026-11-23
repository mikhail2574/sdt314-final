package com.dronesvc.analytics;

import java.util.List;

public interface DataProcessor {

    String name();

    String sensorType();

    List<Finding> process(SensorData data);
}
