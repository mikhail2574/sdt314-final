package com.dronesvc.safety;

import com.dronesvc.mission.GeoPoint;

public class NoFlyZone {

    private final String name;
    private final GeoPoint center;
    private final double radiusKm;

    public NoFlyZone(String name, GeoPoint center, double radiusKm) {
        this.name = name;
        this.center = center;
        this.radiusKm = radiusKm;
    }

    public boolean crosses(GeoPoint from, GeoPoint to) {
        double ax = from.kmEastOf(center);
        double ay = from.kmNorthOf(center);
        double dx = to.kmEastOf(center) - ax;
        double dy = to.kmNorthOf(center) - ay;
        double lengthSq = dx * dx + dy * dy;
        double t = lengthSq == 0 ? 0 : Math.max(0, Math.min(1, -(ax * dx + ay * dy) / lengthSq));
        double px = ax + t * dx;
        double py = ay + t * dy;
        return Math.sqrt(px * px + py * py) <= radiusKm;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s, center %s, radius %.1f km", name, center, radiusKm);
    }
}
