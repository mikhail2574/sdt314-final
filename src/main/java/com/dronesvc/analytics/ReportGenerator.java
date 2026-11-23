package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;
import com.dronesvc.mission.MissionStatus;

import java.util.List;

public abstract class ReportGenerator {

    protected abstract Report createReport(Mission mission, SensorData data, List<Finding> findings);

    public Report generate(Mission mission, SensorData data, List<Finding> findings) {
        if (mission.getStatus() != MissionStatus.PROCESSING) {
            throw new IllegalStateException("Mission " + mission.getId() + " has no processed data yet");
        }
        Report report = createReport(mission, data, findings);
        System.out.println("  " + report.title() + " generated");
        return report;
    }
}
