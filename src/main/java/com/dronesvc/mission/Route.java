package com.dronesvc.mission;

import com.dronesvc.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

public final class Route {

    private final GeoPoint start;
    private final List<GeoPoint> waypoints;
    private final double altitude;
    private final boolean returnToBase;

    private Route(Builder builder) {
        this.start = builder.start;
        this.waypoints = List.copyOf(builder.waypoints);
        this.altitude = builder.altitude;
        this.returnToBase = builder.returnToBase;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<GeoPoint> flightPath() {
        List<GeoPoint> path = new ArrayList<>(waypoints);
        if (returnToBase) {
            path.add(start);
        }
        return path;
    }

    public List<GeoPoint> fullPath() {
        List<GeoPoint> path = new ArrayList<>();
        path.add(start);
        path.addAll(flightPath());
        return path;
    }

    public double areaKm2() {
        double minLat = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = -Double.MAX_VALUE;
        for (GeoPoint p : waypoints) {
            minLat = Math.min(minLat, p.lat());
            maxLat = Math.max(maxLat, p.lat());
            minLon = Math.min(minLon, p.lon());
            maxLon = Math.max(maxLon, p.lon());
        }
        GeoPoint corner = new GeoPoint(minLat, minLon);
        return new GeoPoint(maxLat, minLon).kmNorthOf(corner) * new GeoPoint(minLat, maxLon).kmEastOf(corner);
    }

    public GeoPoint getStart() {
        return start;
    }

    public List<GeoPoint> getWaypoints() {
        return waypoints;
    }

    public double getAltitude() {
        return altitude;
    }

    public boolean isReturnToBase() {
        return returnToBase;
    }

    @Override
    public String toString() {
        return waypoints.size() + " waypoints at " + (int) altitude + " m from " + start
                + (returnToBase ? ", returns to base" : ", ends at last waypoint");
    }

    public static final class Builder {

        private GeoPoint start = AppConfig.getInstance().getBaseLocation();
        private final List<GeoPoint> waypoints = new ArrayList<>();
        private double altitude = AppConfig.getInstance().getDefaultAltitude();
        private boolean returnToBase = true;

        private Builder() {
        }

        public Builder startAt(GeoPoint start) {
            this.start = start;
            return this;
        }

        public Builder addPoint(double lat, double lon) {
            waypoints.add(new GeoPoint(lat, lon));
            return this;
        }

        public Builder altitude(double meters) {
            this.altitude = meters;
            return this;
        }

        public Builder returnToBase(boolean value) {
            this.returnToBase = value;
            return this;
        }

        public Route build() {
            if (waypoints.isEmpty()) {
                throw new IllegalStateException("A route needs at least one waypoint");
            }
            if (altitude <= 0) {
                throw new IllegalStateException("Altitude must be positive");
            }
            return new Route(this);
        }
    }
}
