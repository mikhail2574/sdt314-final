package com.dronesvc.payload;

import com.dronesvc.mission.GeoPoint;

public class CargoBox implements PayloadModule {

    private final double capacityKg;
    private boolean loaded = true;

    public CargoBox(double capacityKg) {
        this.capacityKg = capacityKg;
    }

    @Override
    public String name() {
        return String.format("cargo box %.1f kg", capacityKg);
    }

    @Override
    public double weightKg() {
        return 1.2;
    }

    @Override
    public void onTarget(GeoPoint point) {
        if (loaded) {
            loaded = false;
            System.out.println("    parcel released at " + point);
        }
    }
}
