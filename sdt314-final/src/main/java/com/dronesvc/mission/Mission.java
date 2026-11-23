package com.dronesvc.mission;

import com.dronesvc.fleet.Drone;
import com.dronesvc.request.ServiceRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class Mission {

    private static int nextId = 1;

    private final String id;
    private final ServiceRequest request;
    private final Route route;
    private final LocalDate date;
    private final List<String> equipment;
    private final String notes;
    private MissionStatus status = MissionStatus.PENDING_APPROVAL;
    private Drone drone;

    private Mission(Builder builder) {
        this.id = "M-" + nextId++;
        this.request = builder.request;
        this.route = builder.route;
        this.date = builder.date;
        this.equipment = new ArrayList<>(builder.equipment);
        this.notes = builder.notes;
    }

    private Mission(Mission source, LocalDate date) {
        this.id = "M-" + nextId++;
        this.request = source.request;
        this.route = source.route;
        this.date = date;
        this.equipment = new ArrayList<>(source.equipment);
        this.notes = source.notes;
    }

    public static Builder builder(ServiceRequest request, Route route) {
        return new Builder(request, route);
    }

    public Mission copyForDate(LocalDate newDate) {
        if (newDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot plan a mission in the past");
        }
        return new Mission(this, newDate);
    }

    public void approve() {
        moveTo(MissionStatus.APPROVED, MissionStatus.PENDING_APPROVAL);
    }

    public void reject() {
        moveTo(MissionStatus.CANCELLED, MissionStatus.PENDING_APPROVAL);
    }

    public boolean assignDrone(Drone candidate) {
        if (status != MissionStatus.APPROVED) {
            throw new IllegalStateException("Mission " + id + " is " + status + ", a drone can be assigned only when APPROVED");
        }
        if (!candidate.isReady()) {
            return false;
        }
        drone = candidate;
        status = MissionStatus.SCHEDULED;
        return true;
    }

    public void start() {
        moveTo(MissionStatus.IN_FLIGHT, MissionStatus.SCHEDULED);
    }

    public void abort() {
        moveTo(MissionStatus.APPROVED, MissionStatus.IN_FLIGHT);
        drone = null;
    }

    public void finishFlight() {
        moveTo(MissionStatus.PROCESSING, MissionStatus.IN_FLIGHT);
    }

    public void complete() {
        moveTo(MissionStatus.COMPLETED, MissionStatus.PROCESSING);
    }

    public void cancel() {
        moveTo(MissionStatus.CANCELLED, MissionStatus.PENDING_APPROVAL, MissionStatus.APPROVED, MissionStatus.SCHEDULED);
        drone = null;
    }

    private void moveTo(MissionStatus next, MissionStatus first, MissionStatus... others) {
        if (!EnumSet.of(first, others).contains(status)) {
            throw new IllegalStateException("Mission " + id + " cannot go from " + status + " to " + next);
        }
        status = next;
    }

    public String describe() {
        return "  Mission " + id + " [" + status + "]\n"
                + "    request:   " + request.getId() + " " + request.getType() + " at " + request.getSite() + "\n"
                + "    date:      " + date + "\n"
                + "    route:     " + route + "\n"
                + "    equipment: " + (equipment.isEmpty() ? "-" : String.join(", ", equipment)) + "\n"
                + "    notes:     " + (notes.isEmpty() ? "-" : notes);
    }

    public String getId() {
        return id;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public Route getRoute() {
        return route;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<String> getEquipment() {
        return List.copyOf(equipment);
    }

    public String getNotes() {
        return notes;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public Drone getDrone() {
        return drone;
    }

    public static final class Builder {

        private final ServiceRequest request;
        private final Route route;
        private LocalDate date;
        private final List<String> equipment = new ArrayList<>();
        private String notes = "";

        private Builder(ServiceRequest request, Route route) {
            this.request = Objects.requireNonNull(request, "request");
            this.route = Objects.requireNonNull(route, "route");
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder equipment(String item) {
            equipment.add(item);
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Mission build() {
            if (date == null) {
                date = request.getDate();
            }
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalStateException("Mission date is in the past");
            }
            return new Mission(this);
        }
    }
}
