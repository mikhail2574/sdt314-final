package com.dronesvc;

import com.dronesvc.analytics.InspectionReportGenerator;
import com.dronesvc.analytics.MappingReportGenerator;
import com.dronesvc.analytics.Report;
import com.dronesvc.analytics.RgbPipeline;
import com.dronesvc.analytics.SensorData;
import com.dronesvc.analytics.ThermalPipeline;
import com.dronesvc.config.AppConfig;
import com.dronesvc.fleet.DjiDrone;
import com.dronesvc.fleet.DjiKitFactory;
import com.dronesvc.fleet.Drone;
import com.dronesvc.fleet.DroneConfig;
import com.dronesvc.fleet.SimulatedDrone;
import com.dronesvc.fleet.SimulatedKitFactory;
import com.dronesvc.mission.GeoPoint;
import com.dronesvc.mission.Mission;
import com.dronesvc.mission.MissionService;
import com.dronesvc.mission.Route;
import com.dronesvc.payload.DeliveryPayloadFactory;
import com.dronesvc.payload.InspectionPayloadFactory;
import com.dronesvc.payload.MappingPayloadFactory;
import com.dronesvc.request.Customer;
import com.dronesvc.request.ServiceRequest;
import com.dronesvc.request.ServiceType;
import com.dronesvc.safety.NoFlyZone;
import com.dronesvc.safety.NoFlyZoneRegistry;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        MissionService missions = new MissionService();

        step(1, "Load settings and no-fly zones");
        System.out.println("  settings: " + AppConfig.getInstance());
        NoFlyZoneRegistry zones = NoFlyZoneRegistry.getInstance();
        zones.addZone(new NoFlyZone("Military area", new GeoPoint(52.1000, 21.0300), 1.0));
        zones.addZone(new NoFlyZone("Airport control zone", new GeoPoint(52.1650, 20.9670), 3.0));
        zones.getZones().forEach(zone -> System.out.println("  no-fly zone: " + zone));

        step(2, "Customer submits an inspection request");
        Customer customer = new Customer("Sunfield Energy", "ops@sunfield.example");
        ServiceRequest inspection = new ServiceRequest(customer, ServiceType.INSPECTION,
                "Sunfield solar farm", new GeoPoint(52.1000, 21.0600), tomorrow);
        List<String> errors = inspection.validate();
        System.out.println("  " + inspection + (errors.isEmpty() ? " - valid" : " - invalid: " + errors));

        step(3, "Planner draws a direct route, safety officer checks it");
        Route direct = Route.builder()
                .addPoint(52.1000, 21.0550)
                .addPoint(52.1020, 21.0600)
                .addPoint(52.0980, 21.0600)
                .build();
        Mission draft = Mission.builder(inspection, direct).build();
        System.out.println("  draft " + draft.getId() + ": " + direct);
        missions.review(draft);

        step(4, "Planner builds a detour around the military area");
        Route detour = Route.builder()
                .addPoint(52.1150, 21.0150)
                .addPoint(52.1150, 21.0450)
                .addPoint(52.1020, 21.0600)
                .addPoint(52.0980, 21.0600)
                .addPoint(52.1150, 21.0450)
                .addPoint(52.1150, 21.0150)
                .altitude(50)
                .returnToBase(true)
                .build();
        Mission mission = Mission.builder(inspection, detour)
                .date(tomorrow)
                .equipment("thermal camera")
                .equipment("irradiance sensor")
                .notes("check panel rows A-D for hot spots")
                .build();
        missions.review(mission);
        System.out.println(mission.describe());

        step(5, "Prepare a simulated drone for the inspection");
        DroneConfig reference = new DroneConfig("Standard survey", 120, 15, 1.5,
                List.of("GPS lock", "motor test", "compass calibration"));
        DroneConfig inspectionConfig = reference.copy();
        inspectionConfig.setName("Solar inspection");
        inspectionConfig.addPreflightCheck("geofence upload");
        System.out.println("  reference config: " + reference);
        System.out.println("  copied config:    " + inspectionConfig);
        Drone simDrone = new SimulatedDrone("SIM-01", inspectionConfig, new SimulatedKitFactory());
        System.out.println("  built " + simDrone);
        simDrone.attachPayload(new InspectionPayloadFactory());

        step(6, "Assign the drone and fly the mission");
        missions.assignDrone(mission, simDrone);
        SensorData thermalData = missions.fly(mission);

        step(7, "Process thermal data and send the report");
        Report inspectionReport = missions.deliverResults(mission, thermalData,
                new ThermalPipeline(), new InspectionReportGenerator());
        System.out.println(inspectionReport.render());

        step(8, "Plan the same inspection for next week");
        Mission nextWeek = mission.copyForDate(mission.getDate().plusWeeks(1));
        System.out.println(nextWeek.describe());
        missions.review(nextWeek);
        System.out.println("  original " + mission.getId() + " is still " + mission.getStatus() + " on " + mission.getDate());

        step(9, "Delivery with a drone that has a weak battery");
        ServiceRequest delivery = new ServiceRequest(customer, ServiceType.DELIVERY,
                "Sunfield south substation", new GeoPoint(52.0200, 21.0000), tomorrow);
        Route.Builder southRoute = Route.builder().altitude(80);
        for (int i = 1; i <= 8; i++) {
            southRoute.addPoint(52.1000 - 0.01 * i, 21.0000);
        }
        Mission deliveryMission = Mission.builder(delivery, southRoute.build())
                .equipment("cargo box")
                .notes("spare inverter fuses, 1.5 kg")
                .build();
        missions.review(deliveryMission);

        DroneConfig deliveryConfig = reference.copy();
        deliveryConfig.setName("Heavy delivery");
        deliveryConfig.setMaxPayloadKg(3.0);
        System.out.println("  copied config:    " + deliveryConfig);
        Drone djiDrone = new DjiDrone("DJI-07", deliveryConfig, new DjiKitFactory());
        System.out.println("  built " + djiDrone);
        djiDrone.attachPayload(new DeliveryPayloadFactory());

        djiDrone.getBattery().updateTelemetry(65, 31.0);
        missions.assignDrone(deliveryMission, djiDrone);
        djiDrone.getBattery().updateTelemetry(80, 31.0);
        missions.assignDrone(deliveryMission, djiDrone);
        missions.fly(deliveryMission);
        System.out.println("  " + deliveryMission.getId() + " is " + deliveryMission.getStatus() + ", waiting for a new slot");

        step(10, "Mapping of a construction site");
        ServiceRequest mapping = new ServiceRequest(customer, ServiceType.MAPPING,
                "New plant construction site", new GeoPoint(52.1300, 20.9800), tomorrow.plusDays(1));
        Route grid = Route.builder()
                .addPoint(52.1280, 20.9750)
                .addPoint(52.1280, 20.9850)
                .addPoint(52.1300, 20.9850)
                .addPoint(52.1300, 20.9750)
                .addPoint(52.1320, 20.9750)
                .addPoint(52.1320, 20.9850)
                .altitude(90)
                .build();
        Mission mappingMission = Mission.builder(mapping, grid)
                .equipment("RGB camera")
                .equipment("RTK GPS receiver")
                .build();
        missions.review(mappingMission);
        simDrone.getBattery().recharge();
        simDrone.attachPayload(new MappingPayloadFactory());
        missions.assignDrone(mappingMission, simDrone);
        SensorData images = missions.fly(mappingMission);
        Report mappingReport = missions.deliverResults(mappingMission, images,
                new RgbPipeline(), new MappingReportGenerator());
        System.out.println(mappingReport.render());

        System.out.println("\nDone.");
    }

    private static void step(int number, String title) {
        System.out.println();
        System.out.println("== " + number + ". " + title + " ==");
    }
}
