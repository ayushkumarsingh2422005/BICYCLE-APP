package com.innoreva.pedaller;

public class Bicycle {
    private String location;
    private String description;
    private int costPerHour;
    private String owner;
    private String availability;
    private int imageResourceId;

    public Bicycle(String location, String description, int costPerHour, String owner, String availability, int imageResourceId) {
        this.location = location;
        this.description = description;
        this.costPerHour = costPerHour;
        this.owner = owner;
        this.availability = availability;
        this.imageResourceId = imageResourceId;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public int getCostPerHour() { return costPerHour; }
    public String getOwner() { return owner; }
    public String getAvailability() { return availability; }
    public int getImageResourceId() { return imageResourceId; }
}
