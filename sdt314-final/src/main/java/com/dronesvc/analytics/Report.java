package com.dronesvc.analytics;

import com.dronesvc.mission.Mission;

import java.time.LocalDate;
import java.util.List;

public abstract class Report {

    protected final Mission mission;
    protected final SensorData data;
    protected final List<Finding> findings;
    private final LocalDate createdAt = LocalDate.now();

    protected Report(Mission mission, SensorData data, List<Finding> findings) {
        this.mission = mission;
        this.data = data;
        this.findings = List.copyOf(findings);
    }

    public abstract String title();

    protected abstract List<String> body();

    public String render() {
        StringBuilder text = new StringBuilder();
        text.append("  +--- ").append(title()).append(" ---\n");
        text.append("  | site:    ").append(mission.getRequest().getSite()).append('\n');
        text.append("  | mission: ").append(mission.getId()).append(", date ").append(mission.getDate())
                .append(", created ").append(createdAt).append('\n');
        for (String line : body()) {
            text.append("  | ").append(line).append('\n');
        }
        text.append("  +---");
        return text.toString();
    }

    public List<Finding> getFindings() {
        return findings;
    }
}
