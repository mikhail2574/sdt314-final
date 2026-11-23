package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;

import java.util.List;

public class InspectionReportGenerator extends ReportGenerator {

    @Override
    protected Report createReport(Mission mission, SensorData data, List<Finding> findings) {
        return new InspectionReport(mission, data, findings);
    }
}
