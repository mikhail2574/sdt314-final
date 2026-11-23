package com.dronesvc.fleet;

import com.dronesvc.mission.GeoPoint;

public class DjiFlightController implements FlightController {

    private GeoPoint position;

    @Override
    public String model() {
        return "DJI A3 flight controller";
    }

    @Override
    public void takeOff(GeoPoint from, double altitude) {
        position = from;
        System.out.printf("    [DJI SDK] motors armed, auto take-off from %s to %.0f m%n", from, altitude);
    }

    @Override
    public void flyTo(GeoPoint point) {
        position = point;
    }

    @Override
    public void returnToBase(GeoPoint base) {
        System.out.println("    [DJI SDK] return-to-home from " + position + " to " + base);
        position = base;
    }

    @Override
    public void land(GeoPoint at) {
        position = at;
        System.out.println("    [DJI SDK] precision landing at " + at + ", motors disarmed");
    }
}
