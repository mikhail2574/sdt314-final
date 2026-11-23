package com.dronesvc.fleet;

import com.dronesvc.mission.GeoPoint;

public class DjiDrone extends Drone {

    private static final double MIN_BATTERY_TEMP = 5.0;
    private static final double MAX_BATTERY_TEMP = 40.0;

    public DjiDrone(String droneId, DroneConfig config, DroneKitFactory kit) {
        super(droneId, config, kit);
    }

    @Override
    public boolean isReady() {
        double temp = battery.getTemperature();
        return battery.canStartMission() && temp >= MIN_BATTERY_TEMP && temp <= MAX_BATTERY_TEMP;
    }

    @Override
    protected void returnHome(GeoPoint base) {
        System.out.println("    " + droneId + " battery " + battery.getLevel() + "% is below the safe level, aborting");
        controller.returnToBase(base);
        controller.land(base);
    }
}
