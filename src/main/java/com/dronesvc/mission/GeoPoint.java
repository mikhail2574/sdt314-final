package com.dronesvc.mission;

public record GeoPoint(double lat, double lon) {

    private static final double KM_PER_DEGREE = 111.32;

    public double kmNorthOf(GeoPoint other) {
        return (lat - other.lat) * KM_PER_DEGREE;
    }

    public double kmEastOf(GeoPoint other) {
        double meanLat = Math.toRadians((lat + other.lat) / 2);
        return (lon - other.lon) * KM_PER_DEGREE * Math.cos(meanLat);
    }

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", lat, lon);
    }
}
