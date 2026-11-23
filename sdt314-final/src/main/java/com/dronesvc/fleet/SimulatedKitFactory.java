package com.dronesvc.fleet;

public class SimulatedKitFactory implements DroneKitFactory {

    @Override
    public FlightController createFlightController() {
        return new SimulatedFlightController();
    }

    @Override
    public DroneBattery createBattery() {
        return new DroneBattery("virtual battery", 100, 20.0);
    }
}
