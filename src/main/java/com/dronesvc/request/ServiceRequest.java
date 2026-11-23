package com.dronesvc.request;

import com.dronesvc.mission.GeoPoint;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequest {

    private static int nextId = 1;

    private final String id;
    private final Customer customer;
    private final ServiceType type;
    private final String site;
    private final GeoPoint location;
    private final LocalDate date;
    private RequestStatus status = RequestStatus.SUBMITTED;

    public ServiceRequest(Customer customer, ServiceType type, String site, GeoPoint location, LocalDate date) {
        this.id = "R-" + nextId++;
        this.customer = customer;
        this.type = type;
        this.site = site;
        this.location = location;
        this.date = date;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (customer == null) {
            errors.add("customer is missing");
        }
        if (type == null) {
            errors.add("service type is missing");
        }
        if (location == null) {
            errors.add("location is missing");
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            errors.add("date must be today or later");
        }
        return errors;
    }

    public void updateStatus(RequestStatus newStatus, String note) {
        status = newStatus;
        customer.notify("request " + id + " is " + newStatus + " (" + note + ")");
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ServiceType getType() {
        return type;
    }

    public String getSite() {
        return site;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public RequestStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return id + " " + type + " at " + site + " " + location + " on " + date + " [" + status + "]";
    }
}
