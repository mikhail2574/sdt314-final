package com.dronesvc.fleet;

import com.dronesvc.config.AppConfig;

public class DroneBattery {

    private final String model;
    private int level;
    private double temperature;

    public DroneBattery(String model, int level, double temperature) {
        this.model = model;
        this.level = level;
        this.temperature = temperature;
    }

    public boolean canStartMission() {
        return level >= AppConfig.getInstance().getMinStartBattery();
    }

    public boolean isLow() {
        return level < AppConfig.getInstance().getLowBatteryLevel();
    }

    public void drain(int amount) {
        level = Math.max(0, level - amount);
    }

    public void updateTelemetry(int level, double temperature) {
        this.level = level;
        this.temperature = temperature;
        System.out.printf("  telemetry %s: battery %d%%, %.1f C%n", model, level, temperature);
    }

    public void recharge() {
        level = 100;
    }

    public String getModel() {
        return model;
    }

    public int getLevel() {
        return level;
    }

    public double getTemperature() {
        return temperature;
    }
}
