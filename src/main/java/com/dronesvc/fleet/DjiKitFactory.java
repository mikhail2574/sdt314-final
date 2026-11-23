package com.dronesvc.fleet;

public class DjiKitFactory implements DroneKitFactory {

    @Override
    public FlightController createFlightController() {
        return new DjiFlightController();
    }

    @Override
    public DroneBattery createBattery() {
        return new DroneBattery("DJI TB65", 100, 25.0);
    }
}
