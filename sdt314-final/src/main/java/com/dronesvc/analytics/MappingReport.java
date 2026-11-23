package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;

import java.util.ArrayList;
import java.util.List;

public class MappingReport extends Report {

    private final double areaKm2;

    public MappingReport(Mission mission, SensorData data, List<Finding> findings, double areaKm2) {
        super(mission, data, findings);
        this.areaKm2 = areaKm2;
    }

    @Override
    public String title() {
        return "Mapping report " + mission.getId();
    }

    @Override
    protected List<String> body() {
        List<String> lines = new ArrayList<>();
        lines.add(String.format("covered area: %.2f km2", areaKm2));
        lines.add("images in the orthomosaic: " + (data.size() - findings.size()) + " of " + data.size());
        for (Finding finding : findings) {
            lines.add("  - " + finding);
        }
        lines.add(findings.isEmpty() ? "map is complete" : "map has gaps, re-shoot the listed points");
        return lines;
    }
}
