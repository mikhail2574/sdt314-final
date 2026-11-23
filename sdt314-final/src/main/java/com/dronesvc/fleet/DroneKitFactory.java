package com.dronesvc.fleet;

public interface DroneKitFactory {

    FlightController createFlightController();

    DroneBattery createBattery();
}
