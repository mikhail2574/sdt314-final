package com.dronesvc.payload;

import com.dronesvc.analytics.SensorData;
import com.dronesvc.mission.GeoPoint;

public interface Camera {

    String model();

    String sensorType();

    double weightKg();

    void capture(GeoPoint point, int frame, SensorData data);
}
