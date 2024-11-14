import Cycle from "../models/Cycle.js";
import User from "../models/User.js";
import moment from "moment";

export const addCycle = async (req, res) => {
    const { location, slot, model, hourlyRate } = req.body;
    const ownerId = req.userId;

    if (!location || !slot || !model || hourlyRate == null) {
        return res.status(400).json({ message: "All fields are required" });
    }

    try {
        const [startTimeStr, endTimeStr] = slot.split(' - ');
        
        const startTime = moment(startTimeStr, "HH:mm");
        const endTime = moment(endTimeStr, "HH:mm");
        const currentTime = moment();

        const isAvailable = currentTime.isBetween(startTime, endTime, null, '[]');

        const newCycle = new Cycle({
            ownerId,
            location,
            slot,
            model,
            hourlyRate,
            isAvailable
        });

        await newCycle.save();
        res.status(201).json({ message: "Cycle added successfully", cycle: newCycle });

    } catch (error) {
        console.error("Error adding cycle:", error);
        res.status(500).json({ message: "Error adding cycle", error: error.message });
    }
};

export const updateCycle = async (req, res) => {
    const { cycleId } = req.params;
    const updates = req.body;

    try {
        const cycle = await Cycle.findById(cycleId);
        if (!cycle) return res.status(404).json({ message: "Cycle not found" });
        
        if (cycle.ownerId.toString() !== req.userId) {
            return res.status(403).json({ message: "Not authorized" });
        }

        Object.assign(cycle, updates);
        await cycle.save();
        res.json({ message: "Cycle updated successfully", cycle });
    } catch (error) {
        res.status(500).json({ message: "Error updating cycle" });
    }
};

export const getAllAvailableCycles = async (req, res) => {
    try {
        const cycles = await Cycle.find({ isAvailable: true })
            .populate('ownerId', 'userName firstName lastName phoneNumber');
        res.json(cycles);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: "Error fetching cycles" });
    }
};

export const getUserCycles = async (req, res) => {
    const ownerId = req.userId;
    try {
        const cycles = await Cycle.find({ ownerId })
            .populate('ownerId', 'userName firstName lastName phoneNumber');
        res.json(cycles);
    } catch (error) {
        console.log(error);
        res.status(500).json({ message: "Error fetching cycles" });
    }
};

export const deleteCycle = async (req, res) => {
    const { cycleId } = req.params;
    
    try {
        const cycle = await Cycle.findById(cycleId);
        if (!cycle) {
            return res.status(404).json({ message: "Cycle not found" });
        }

        if (cycle.ownerId.toString() !== req.userId) {
            return res.status(403).json({ message: "Not authorized" });
        }

        await Cycle.findByIdAndDelete(cycleId);
        res.json({ message: "Cycle deleted successfully" });
    } catch (error) {
        res.status(500).json({ message: "Error deleting cycle" });
    }
};

export const getCycleDetails = async (req, res) => {
    const { cycleId } = req.params;
    
    try {
        const cycle = await Cycle.findById(cycleId)
            .populate('ownerId', 'userName phoneNumber');
            
        if (!cycle) {
            return res.status(404).json({ message: "Cycle not found" });
        }
        
        res.json(cycle);
    } catch (error) {
        res.status(500).json({ message: "Error fetching cycle details" });
    }
};
