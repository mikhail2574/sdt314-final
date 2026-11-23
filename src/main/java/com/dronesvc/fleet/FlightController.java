package com.dronesvc.fleet;

import com.dronesvc.mission.GeoPoint;

public interface FlightController {

    String model();

    void takeOff(GeoPoint from, double altitude);

    void flyTo(GeoPoint point);

    void returnToBase(GeoPoint base);

    void land(GeoPoint at);
}
