package com.dronesvc.fleet;

import com.dronesvc.analytics.SensorData;
import com.dronesvc.config.AppConfig;
import com.dronesvc.mission.GeoPoint;
import com.dronesvc.mission.Route;
import com.dronesvc.payload.Camera;
import com.dronesvc.payload.PayloadFactory;
import com.dronesvc.payload.PayloadModule;

import java.util.List;

public abstract class Drone {

    protected final String droneId;
    protected final DroneConfig config;
    protected final FlightController controller;
    protected final DroneBattery battery;
    private Camera camera;
    private PayloadModule module;
    private SensorData collected;

    protected Drone(String droneId, DroneConfig config, DroneKitFactory kit) {
        this.droneId = droneId;
        this.config = config;
        this.controller = kit.createFlightController();
        this.battery = kit.createBattery();
    }

    public abstract boolean isReady();

    protected abstract void returnHome(GeoPoint base);

    public void attachPayload(PayloadFactory payload) {
        Camera newCamera = payload.createCamera();
        PayloadModule newModule = payload.createModule();
        double weight = newCamera.weightKg() + newModule.weightKg();
        if (weight > config.getMaxPayloadKg()) {
            throw new IllegalStateException(String.format("%s cannot carry %.1f kg, limit is %.1f kg",
                    droneId, weight, config.getMaxPayloadKg()));
        }
        camera = newCamera;
        module = newModule;
        System.out.printf("  %s payload: %s + %s (%.1f of %.1f kg)%n",
                droneId, camera.model(), module.name(), weight, config.getMaxPayloadKg());
    }

    public boolean fly(Route route) {
        if (camera == null) {
            throw new IllegalStateException(droneId + " has no payload");
        }
        int drainPerStep = AppConfig.getInstance().getDrainPerStep();
        collected = new SensorData(camera.sensorType());
        System.out.println("    " + droneId + " pre-flight: " + String.join(", ", config.getPreflightChecks()) + " - OK");
        controller.takeOff(route.getStart(), route.getAltitude());

        List<GeoPoint> path = route.flightPath();
        int targets = route.getWaypoints().size();
        for (int i = 0; i < path.size(); i++) {
            GeoPoint point = path.get(i);
            controller.flyTo(point);
            battery.drain(drainPerStep);
            System.out.printf("    step %d/%d  %s  battery %d%%%n", i + 1, path.size(), point, battery.getLevel());
            if (battery.isLow()) {
                returnHome(route.getStart());
                return false;
            }
            if (i < targets) {
                camera.capture(point, i + 1, collected);
            }
            if (i == targets - 1) {
                module.onTarget(point);
            }
        }
        controller.land(route.isReturnToBase() ? route.getStart() : path.get(path.size() - 1));
        return true;
    }

    public SensorData uploadData() {
        System.out.println("  " + droneId + " uploaded " + collected.size() + " " + collected.getSensorType() + " files");
        return collected;
    }

    public String getDroneId() {
        return droneId;
    }

    public DroneConfig getConfig() {
        return config;
    }

    public DroneBattery getBattery() {
        return battery;
    }

    @Override
    public String toString() {
        return droneId + " (" + controller.model() + ", " + battery.getModel() + ", config \"" + config.getName() + "\")";
    }
}
