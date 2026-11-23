package com.dronesvc.fleet;

import java.util.ArrayList;
import java.util.List;

public class DroneConfig {

    private String name;
    private double maxAltitude;
    private double maxSpeed;
    private double maxPayloadKg;
    private final List<String> preflightChecks;

    public DroneConfig(String name, double maxAltitude, double maxSpeed, double maxPayloadKg, List<String> preflightChecks) {
        this.name = name;
        this.maxAltitude = maxAltitude;
        this.maxSpeed = maxSpeed;
        this.maxPayloadKg = maxPayloadKg;
        this.preflightChecks = new ArrayList<>(preflightChecks);
    }

    private DroneConfig(DroneConfig source) {
        this.name = source.name;
        this.maxAltitude = source.maxAltitude;
        this.maxSpeed = source.maxSpeed;
        this.maxPayloadKg = source.maxPayloadKg;
        this.preflightChecks = new ArrayList<>(source.preflightChecks);
    }

    public DroneConfig copy() {
        return new DroneConfig(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAltitude(double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMaxPayloadKg(double maxPayloadKg) {
        this.maxPayloadKg = maxPayloadKg;
    }

    public void addPreflightCheck(String check) {
        preflightChecks.add(check);
    }

    public String getName() {
        return name;
    }

    public double getMaxAltitude() {
        return maxAltitude;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getMaxPayloadKg() {
        return maxPayloadKg;
    }

    public List<String> getPreflightChecks() {
        return List.copyOf(preflightChecks);
    }

    @Override
    public String toString() {
        return String.format("%s: max %.0f m, %.0f m/s, payload %.1f kg, checks %s",
                name, maxAltitude, maxSpeed, maxPayloadKg, preflightChecks);
    }
}
