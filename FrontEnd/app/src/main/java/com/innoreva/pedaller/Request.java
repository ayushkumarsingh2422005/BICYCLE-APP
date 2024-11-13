package com.innoreva.pedaller;

public class Request {
    private String name;
    private String registrationNumber;
    private String timeSlot;

    public Request(String name, String registrationNumber, String timeSlot) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.timeSlot = timeSlot;
    }

    public String getName() {
        return name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getTimeSlot() {
        return timeSlot;
    }
}
