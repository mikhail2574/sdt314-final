package com.dronesvc.payload;

import com.dronesvc.analytics.SensorData;
import com.dronesvc.mission.GeoPoint;

import java.util.Random;

public class RgbCamera implements Camera {

    private final Random sensor = new Random(11);

    @Override
    public String model() {
        return "20 MP RGB camera";
    }

    @Override
    public String sensorType() {
        return SensorData.RGB;
    }

    @Override
    public double weightKg() {
        return 0.6;
    }

    @Override
    public void capture(GeoPoint point, int frame, SensorData data) {
        double sharpness = sensor.nextInt(5) == 0
                ? 0.40 + sensor.nextDouble() * 0.2
                : 0.80 + sensor.nextDouble() * 0.2;
        data.add(String.format("img_%03d.jpg", frame), point, sharpness);
    }
}
