import User from "../models/User.js";

export const riderName = async (req, res) => {
    try {
        if (!req.userId) {
            return res.status(400).json({ message: "User ID is required" });
        }
        const user = await User.findById(req.userId).select("-password");

        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        res.status(200).json({
            userId: user._id,
            userName: user.userName,
            firstName: user.firstName,
            lastName: user.lastName,
            regNumber: user.regNumber,
            phoneNumber: user.phoneNumber,
            hostel: user.hostel,
            roomNumber: user.roomNumber,
            currentLocation: user.currentLocation,
            email: user.email,
        });
    } catch (error) {
        console.error("Error fetching rider information:", error);
        res.status(500).json({ message: "Server error" });
    }
};
