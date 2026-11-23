package com.dronesvc.payload;

import com.dronesvc.analytics.SensorData;
import com.dronesvc.mission.GeoPoint;

import java.util.Random;

public class ThermalCamera implements Camera {

    private final Random sensor = new Random(10);

    @Override
    public String model() {
        return "thermal camera 640x512";
    }

    @Override
    public String sensorType() {
        return SensorData.THERMAL;
    }

    @Override
    public double weightKg() {
        return 0.9;
    }

    @Override
    public void capture(GeoPoint point, int frame, SensorData data) {
        double celsius = sensor.nextInt(4) == 0
                ? 70 + sensor.nextDouble() * 15
                : 28 + sensor.nextDouble() * 8;
        data.add(String.format("thermal_%03d.tiff", frame), point, celsius);
    }
}
