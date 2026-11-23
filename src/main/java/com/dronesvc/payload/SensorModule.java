package com.dronesvc.payload;

import com.dronesvc.mission.GeoPoint;

public class SensorModule implements PayloadModule {

    private final String name;
    private final double weightKg;

    public SensorModule(String name, double weightKg) {
        this.name = name;
        this.weightKg = weightKg;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public double weightKg() {
        return weightKg;
    }

    @Override
    public void onTarget(GeoPoint point) {
        System.out.println("    " + name + " saved a reference reading at " + point);
    }
}
