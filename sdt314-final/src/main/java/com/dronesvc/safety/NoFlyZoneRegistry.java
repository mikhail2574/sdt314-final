package com.dronesvc.safety;

import com.dronesvc.mission.GeoPoint;
import com.dronesvc.mission.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class NoFlyZoneRegistry {

    private final List<NoFlyZone> zones = new CopyOnWriteArrayList<>();

    private NoFlyZoneRegistry() {
    }

    private static final class Holder {
        private static final NoFlyZoneRegistry INSTANCE = new NoFlyZoneRegistry();
    }

    public static NoFlyZoneRegistry getInstance() {
        return Holder.INSTANCE;
    }

    public void addZone(NoFlyZone zone) {
        zones.add(zone);
    }

    public List<NoFlyZone> getZones() {
        return List.copyOf(zones);
    }

    public List<NoFlyZone> findZones(Route route) {
        List<GeoPoint> path = route.fullPath();
        List<NoFlyZone> crossed = new ArrayList<>();
        for (NoFlyZone zone : zones) {
            for (int i = 1; i < path.size(); i++) {
                if (zone.crosses(path.get(i - 1), path.get(i))) {
                    crossed.add(zone);
                    break;
                }
            }
        }
        return crossed;
    }
}
