package com.dronesvc.fleet;

import com.dronesvc.mission.GeoPoint;

public class SimulatedDrone extends Drone {

    public SimulatedDrone(String droneId, DroneConfig config, DroneKitFactory kit) {
        super(droneId, config, kit);
    }

    @Override
    public boolean isReady() {
        return battery.canStartMission();
    }

    @Override
    protected void returnHome(GeoPoint base) {
        System.out.println("    " + droneId + " battery low, simulation sends the drone home");
        controller.returnToBase(base);
        controller.land(base);
    }
}
