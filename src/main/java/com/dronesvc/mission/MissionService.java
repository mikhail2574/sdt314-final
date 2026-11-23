package com.dronesvc.mission;

import com.dronesvc.analytics.DataPipeline;
import com.dronesvc.analytics.Finding;
import com.dronesvc.analytics.Report;
import com.dronesvc.analytics.ReportGenerator;
import com.dronesvc.analytics.SensorData;
import com.dronesvc.fleet.Drone;
import com.dronesvc.request.RequestStatus;
import com.dronesvc.safety.ComplianceCheck;

import java.util.List;

public class MissionService {

    public boolean review(Mission mission) {
        ComplianceCheck check = new ComplianceCheck(mission.getRoute());
        boolean clear = check.run();
        if (clear) {
            mission.approve();
        } else {
            mission.reject();
        }
        System.out.println("  safety check for " + mission.getId() + ": " + check.getReason() + " -> " + mission.getStatus());
        if (clear) {
            mission.getRequest().updateStatus(RequestStatus.APPROVED, "mission " + mission.getId() + " on " + mission.getDate());
        }
        return clear;
    }

    public boolean assignDrone(Mission mission, Drone drone) {
        boolean accepted = mission.assignDrone(drone);
        System.out.println("  assign " + drone.getDroneId() + " (battery " + drone.getBattery().getLevel() + "%) to "
                + mission.getId() + ": " + (accepted ? "accepted -> " + mission.getStatus() : "refused, drone is not ready"));
        if (accepted) {
            mission.getRequest().updateStatus(RequestStatus.SCHEDULED, "drone " + drone.getDroneId() + " assigned");
        }
        return accepted;
    }

    public SensorData fly(Mission mission) {
        Drone drone = mission.getDrone();
        mission.start();
        mission.getRequest().updateStatus(RequestStatus.IN_PROGRESS, "mission " + mission.getId() + " is " + mission.getStatus());
        boolean finished = drone.fly(mission.getRoute());
        if (!finished) {
            mission.abort();
            mission.getRequest().updateStatus(RequestStatus.RESCHEDULED,
                    "drone returned early, mission " + mission.getId() + " is " + mission.getStatus() + " again");
            return null;
        }
        mission.finishFlight();
        System.out.println("  mission " + mission.getId() + " -> " + mission.getStatus());
        return drone.uploadData();
    }

    public Report deliverResults(Mission mission, SensorData data, DataPipeline pipeline, ReportGenerator generator) {
        List<Finding> findings = pipeline.run(data);
        Report report = generator.generate(mission, data, findings);
        mission.complete();
        System.out.println("  mission " + mission.getId() + " -> " + mission.getStatus());
        mission.getRequest().updateStatus(RequestStatus.COMPLETED, report.title() + " is ready");
        return report;
    }
}
