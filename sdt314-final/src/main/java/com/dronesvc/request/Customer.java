package com.dronesvc.request;

public class Customer {

    private final String name;
    private final String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void notify(String message) {
        System.out.println("  -> " + name + " <" + email + ">: " + message);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
