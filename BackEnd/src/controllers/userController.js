import User from "../models/User.js";

export const getUserData = async (req, res) => {
    const userId  = req.userId;

    try {
        const user = await User.findById(userId).select("-password");

        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        res.status(200).json(user);
    } catch (error) {
        console.error("Get user data error:", error);
        res.status(500).json({ message: "Server error" });
    }
};