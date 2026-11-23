package com.dronesvc.config;

import com.dronesvc.mission.GeoPoint;

public final class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();

    private final int minStartBattery = 80;
    private final int lowBatteryLevel = 20;
    private final int drainPerStep = 10;
    private final double defaultAltitude = 60.0;
    private final double maxAltitude = 120.0;
    private final GeoPoint baseLocation = new GeoPoint(52.1000, 21.0000);

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public int getMinStartBattery() {
        return minStartBattery;
    }

    public int getLowBatteryLevel() {
        return lowBatteryLevel;
    }

    public int getDrainPerStep() {
        return drainPerStep;
    }

    public double getDefaultAltitude() {
        return defaultAltitude;
    }

    public double getMaxAltitude() {
        return maxAltitude;
    }

    public GeoPoint getBaseLocation() {
        return baseLocation;
    }

    @Override
    public String toString() {
        return String.format("base %s, start battery >= %d%%, return home below %d%%, drain %d%% per step, max altitude %.0f m",
                baseLocation, minStartBattery, lowBatteryLevel, drainPerStep, maxAltitude);
    }
}
