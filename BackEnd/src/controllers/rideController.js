import Ride from "../models/Ride.js";
import Cycle from "../models/Cycle.js";
import User from "../models/User.js";

export const requestRide = async (req, res) => {
    const userId = req.userId;
    const { cycleId,startLocation } = req.body;

    try {
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

        const ongoingRide = await Ride.findOne({ userId, endLocation: null });
        if (ongoingRide) {
            return res.status(400).json({ message: "Please wait while the owner confirms your ride" });
        }

        const formattedStartLocation = {
            x: startLocation.longitude.toString(),
            y: startLocation.latitude.toString() 
        };

        const newRide = new Ride({
            userId,
            cycleId,
            startLocation: formattedStartLocation
        });

        cycle.isAvailable = true;
        await cycle.save();

        await newRide.save();
        res.status(201).json({ 
            message: "Ride requested successfully", 
            rideId: newRide._id 
        });

    } catch (error) {
        console.error("Start request error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const acceptRide = async (req, res) => {
    const userId = req.userId;
    const { rideId } = req.body;

    try {
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        const ride = await Ride.findById(rideId);
        if (!ride) {
            return res.status(404).json({ message: "Ride not found" });
        }

        const cycle = await Cycle.findById(ride.cycleId);
        if (!cycle) {
            return res.status(404).json({ message: "Cycle not found" });
        }

        if (cycle.ownerId.toString() !== userId.toString()) {
            return res.status(403).json({ message: "You are not authorized to accept this ride" });
        }

        ride.startTime = new Date();
        ride.isActive = true;
        cycle.isAvailable = false;
        await ride.save();
        await cycle.save();
        
        res.status(200).json({ 
            message: "Ride accepted successfully", 
            rideId: ride._id 
        });

    } catch (error) {
        console.error("Ride acccept error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const declineRide = async (req, res) => {
    const userId = req.userId;
    const { rideId } = req.body;

    try {
        const ride = await Ride.findById(rideId);
        if (!ride) {
            return res.status(404).json({ message: "Ride not found" });
        }

        const cycle = await Cycle.findById(ride.cycleId);
        if (!cycle) {
            return res.status(404).json({ message: "Cycle not found" });
        }

        if (cycle.ownerId.toString() !== userId.toString()) {
            return res.status(403).json({ message: "You are not authorized to decline this ride" });
        }

        await Ride.findByIdAndDelete(rideId);

        cycle.isAvailable = true;
        await cycle.save();

        res.status(200).json({ 
            message: "Ride declined and deleted successfully", 
            rideId: ride._id 
        });

    } catch (error) {
        console.error("Ride decline error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const ridesNotification = async (req, res) => {
    const userId = req.userId;

    try {
        const cycles = await Cycle.find({ ownerId: userId });
        if (!cycles.length) {
            return res.status(404).json({ message: "No cycles found for this user" });
        }

        const cyclesWithRides = await Promise.all(cycles.map(async (cycle) => {
            const rides = await Ride.find({ cycleId: cycle._id }).sort({ startTime: 1 });
            
            const ridesWithDetails = await Promise.all(rides.map(async (ride) => {
                const rider = await User.findById(ride.userId);
                return {
                    rideId: ride._id,
                    riderName: rider 
                    ? (rider.firstName && rider.lastName 
                        ? `${rider.firstName} ${rider.lastName}` 
                        : rider.userName || "Unknown Rider")
                    : "Unknown Rider",
                    riderPhoneNumber: rider ? rider.phoneNumber : "N/A",
                    riderLocation: ride.startLocation,
                    cycleLocation: cycle.location,
                    cycleId: cycle._id,
                    cycleName: cycle.model,
                    startTime: ride.startTime
                };
            }));

            return ridesWithDetails;
        }));

        const result = cyclesWithRides.flat();

        res.status(200).json(result);
        
    } catch (error) {
        console.error("Error fetching cycles with rides:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const endRide = async (req, res) => {
    const userId = req.userId;
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
        if (ride.userId.toString() !== userId) {
            return res.status(403).json({ message: "You are not authorized to end this ride" });
        }

        cycle.isAvailable = true;
        await cycle.save();

        ride.endLocation = endLocation;
        ride.endTime = Date.now();
        ride.isActive = false;

        const duration = (ride.endTime - ride.startTime) / (1000 * 60 * 60);
        const durationInHumanReadable = formatDuration(duration);
        ride.cost = parseFloat((duration * cycle.hourlyRate).toFixed(2));

        await ride.save();

        res.status(200).json({ message: "Ride ended successfully", cost: ride.cost, duration: durationInHumanReadable });
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
            .populate("cycleId","location");

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
    const userId = req.userId;

    try {
        const rides = await Ride.find({ userId })
            .populate({
                path: 'cycleId',
                select: 'model hourlyRate',
                populate: {
                    path: 'ownerId',
                    select: 'userName firstName lastName phoneNumber'
                }
            })
            .sort({ startTime: -1 });

        if (!rides || rides.length === 0) {
            return res.status(404).json({ message: "No rides found for this user" });
        }

        const formattedRides = rides.map(ride => {
            const isActive = ride.endTime == null && ride.endLocation !== null;

            return {
                rideId: ride._id,
                startTime: ride.startTime,
                endTime: ride.endTime,
                cost: ride.cost,
                isActive,
                cycle: {
                    id: ride.cycleId._id,
                    rate: ride.cycleId.hourlyRate,
                    model: ride.cycleId.model,
                    owner: {
                        userName: ride.cycleId.ownerId.userName,
                        firstName: ride.cycleId.ownerId.firstName,
                        lastName: ride.cycleId.ownerId.lastName,
                        phoneNumber: ride.cycleId.ownerId.phoneNumber
                    }
                }
            };
        });

        const activeRides = formattedRides.filter(ride => ride.isActive);

        res.status(200).json(activeRides);
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

const formatDuration = (durationInMinutes) => {
    const hours = Math.floor(durationInMinutes / 60);
    const minutes = durationInMinutes % 60;

    let formattedDuration = "";
    if (hours > 0) {
        formattedDuration += `${hours} hr${hours > 1 ? 's' : ''}`;
    }
    if (minutes > 0) {
        if (formattedDuration) {
            formattedDuration += " ";
        }
        formattedDuration += `${minutes} min${minutes > 1 ? 's' : ''}`;
    }

    return formattedDuration || "0 mins";
};