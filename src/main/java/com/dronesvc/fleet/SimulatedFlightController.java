package com.dronesvc.fleet;

import com.dronesvc.mission.GeoPoint;

public class SimulatedFlightController implements FlightController {

    private GeoPoint position;

    @Override
    public String model() {
        return "virtual flight controller";
    }

    @Override
    public void takeOff(GeoPoint from, double altitude) {
        position = from;
        System.out.printf("    [sim] take-off from %s, climbing to %.0f m%n", from, altitude);
    }

    @Override
    public void flyTo(GeoPoint point) {
        position = point;
    }

    @Override
    public void returnToBase(GeoPoint base) {
        System.out.println("    [sim] flying back from " + position + " to " + base);
        position = base;
    }

    @Override
    public void land(GeoPoint at) {
        position = at;
        System.out.println("    [sim] landed at " + at);
    }
}
