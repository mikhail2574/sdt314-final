package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;

import java.util.ArrayList;
import java.util.List;

public class InspectionReport extends Report {

    public InspectionReport(Mission mission, SensorData data, List<Finding> findings) {
        super(mission, data, findings);
    }

    @Override
    public String title() {
        return "Inspection report " + mission.getId();
    }

    @Override
    protected List<String> body() {
        List<String> lines = new ArrayList<>();
        lines.add("thermal images analysed: " + data.size());
        lines.add("defects found: " + findings.size());
        for (Finding finding : findings) {
            lines.add("  - " + finding);
        }
        lines.add(findings.isEmpty() ? "verdict: no maintenance needed" : "verdict: send a technician to the listed points");
        return lines;
    }
}
