package com.innoreva.pedaller;

public class Cycle {
    private String location;
    private String costPerHour;
    private String availableTime;
    private int imageResourceId;

    public Cycle(String location, String costPerHour, String availableTime, int imageResourceId) {
        this.location = location;
        this.costPerHour = costPerHour;
        this.availableTime = availableTime;
        this.imageResourceId = imageResourceId;
    }

    public String getLocation() {
        return location;
    }

    public String getCostPerHour() {
        return costPerHour;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
