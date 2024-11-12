import Ride from "../models/Ride.js";
import Cycle from "../models/Cycle.js";
import User from "../models/User.js";

export const startRide = async (req, res) => {
    const { userId, cycleId, startLocation } = req.body;

    const user = await User.findById(userId);
    if (!user) {
        return res.status(404).json({ message: "User not found" });
    }

    const cycle = await Cycle.findById(cycleId);
    if (!cycle) {
        return res.status(404).json({ message: "Cycle not found" });
    }

    if (!cycle.isAvailable) {
        return res.status(400).json({ message: "Cycle is currently unavailable" });
    }

    try {
        const newRide = new Ride({
            userId,
            cycleId,
            startLocation,
            startTime: Date.now(),
        });

        cycle.isAvailable = false;
        await cycle.save();

        await newRide.save();

        res.status(201).json({ message: "Ride started successfully", rideId: newRide._id });
    } catch (error) {
        console.error("Start ride error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const endRide = async (req, res) => {
    const { rideId, endLocation } = req.body;

    try {
        const ride = await Ride.findById(rideId);
        if (!ride || !ride.isActive) {
            return res.status(400).json({ message: "Ride not found or already ended" });
        }

        const cycle = await Cycle.findById(ride.cycleId);
        if (!cycle) {
            return res.status(404).json({ message: "Cycle not found" });
        }

        cycle.isAvailable = true;
        await cycle.save();

        ride.endLocation = endLocation;
        ride.endTime = Date.now();
        ride.isActive = false;

        const duration = (ride.endTime - ride.startTime) / (1000 * 60 * 60);
        ride.cost = parseFloat((duration * cycle.hourlyRate).toFixed(2));

        await ride.save();

        res.status(200).json({ message: "Ride ended successfully", cost: ride.cost });
    } catch (error) {
        console.error("End ride error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const getRideDetails = async (req, res) => {
    const { rideId } = req.params;

    try {
        const ride = await Ride.findById(rideId)
            .populate("userId", "userName")
            .populate("cycleId");

        if (!ride) {
            return res.status(404).json({ message: "Ride not found" });
        }

        res.status(200).json(ride);
    } catch (error) {
        console.error("Get ride details error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const getUserRides = async (req, res) => {
    const { userId } = req.params;

    try {
        const rides = await Ride.find({ userId }).sort({ startTime: -1 });

        if (!rides || rides.length === 0) {
            return res.status(404).json({ message: "No rides found for this user" });
        }

        res.status(200).json(rides);
    } catch (error) {
        console.error("Get user rides error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const getAllActiveRides = async (req, res) => {
    try {
        const activeRides = await Ride.find({ isActive: true });

        if (!activeRides || activeRides.length === 0) {
            return res.status(404).json({ message: "No active rides available" });
        }

        res.status(200).json(activeRides);
    } catch (error) {
        console.error("Get all active rides error:", error);
        res.status(500).json({ message: "Server error" });
    }
};
