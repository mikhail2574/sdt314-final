package com.dronesvc.analytics;

import com.dronesvc.mission.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class SensorData {

    public static final String THERMAL = "THERMAL";
    public static final String RGB = "RGB";

    public record Sample(String file, GeoPoint point, double value) {
    }

    private final String sensorType;
    private final List<Sample> samples = new ArrayList<>();

    public SensorData(String sensorType) {
        this.sensorType = sensorType;
    }

    public void add(String file, GeoPoint point, double value) {
        samples.add(new Sample(file, point, value));
    }

    public String getSensorType() {
        return sensorType;
    }

    public List<Sample> getSamples() {
        return List.copyOf(samples);
    }

    public List<String> getFiles() {
        return samples.stream().map(Sample::file).toList();
    }

    public int size() {
        return samples.size();
    }
}
