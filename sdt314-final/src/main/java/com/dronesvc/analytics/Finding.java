package com.dronesvc.analytics;

import com.dronesvc.mission.GeoPoint;

public record Finding(GeoPoint location, String description) {

    @Override
    public String toString() {
        return location + " " + description;
    }
}
