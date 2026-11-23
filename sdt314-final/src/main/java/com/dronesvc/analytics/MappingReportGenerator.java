package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;

import java.util.List;

public class MappingReportGenerator extends ReportGenerator {

    @Override
    protected Report createReport(Mission mission, SensorData data, List<Finding> findings) {
        return new MappingReport(mission, data, findings, mission.getRoute().areaKm2());
    }
}
