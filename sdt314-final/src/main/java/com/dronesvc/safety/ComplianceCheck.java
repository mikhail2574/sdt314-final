package com.dronesvc.safety;

import com.dronesvc.config.AppConfig;
import com.dronesvc.mission.Route;

import java.util.List;
import java.util.stream.Collectors;

public class ComplianceCheck {

    private final Route route;
    private boolean approved;
    private String reason = "not checked yet";

    public ComplianceCheck(Route route) {
        this.route = route;
    }

    public boolean run() {
        double maxAltitude = AppConfig.getInstance().getMaxAltitude();
        if (route.getAltitude() > maxAltitude) {
            approved = false;
            reason = "altitude " + (int) route.getAltitude() + " m is above the " + (int) maxAltitude + " m limit";
            return false;
        }
        List<NoFlyZone> crossed = NoFlyZoneRegistry.getInstance().findZones(route);
        approved = crossed.isEmpty();
        reason = approved
                ? "route is clear of all no-fly zones"
                : "route crosses " + crossed.stream().map(NoFlyZone::getName).collect(Collectors.joining(", "));
        return approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getReason() {
        return reason;
    }
}
