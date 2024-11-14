import User from "../models/User.js";

export const getProfileDetails = async (req, res) => {
    const userId = req.userId;
    
    try {
        const user = await User.findById(userId)
            .select([
                'userName',
                'firstName',
                'lastName',
                'regNumber',
                'phoneNumber',
                'hostel',
                'roomNumber',
                'email',
                'isProfileComplete',
                'currentLocation'
            ]);

        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        res.status(200).json({
                id: userId,
                userName: user.userName,
                firstName: user.firstName,
                lastName: user.lastName,
                regNumber: user.regNumber,
                phoneNumber: user.phoneNumber,
                hostel: user.hostel,
                roomNumber: user.roomNumber,
                email: user.email,
                isProfileComplete: user.isProfileComplete,
                role: user.role,
                currentLocation: user.currentLocation
        });
    } catch (error) {
        console.error("Get profile details error:", error);
        res.status(500).json({ message: "Error fetching profile details" });
    }
};

export const updateProfile = async (req, res) => {
    const userId = req.userId;
    const updates = req.body;
    
    const restrictedFields = ['password', 'email', 'role', 'isVerified'];
    restrictedFields.forEach(field => delete updates[field]);

    try {
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        Object.assign(user, updates);
        
        const requiredFields = ['firstName', 'lastName', 'phoneNumber', 'regNumber', 'hostel', 'roomNumber'];
        user.isProfileComplete = requiredFields.every(field => user[field]);

        await user.save();
        res.json({ message: "Profile updated successfully", user: user });
    } catch (error) {
        res.status(500).json({ message: "Error updating profile" });
    }
};

export const getUserStats = async (req, res) => {
    const userId = req.userId;
    try {
        const user = await User.findById(userId)
            .select('ratings')
            .populate({
                path: 'ratings.fromUserId',
                select: 'userName'
            });

        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }
        const averageRating = user.ratings.length > 0
            ? user.ratings.reduce((acc, curr) => acc + curr.rating, 0) / user.ratings.length
            : 0;
        const totalRatings = user.ratings.length;

        res.json({
            averageRating,
            totalRatings,
            ratingDistribution: {
                5: user.ratings.filter(r => r.rating === 5).length,
                4: user.ratings.filter(r => r.rating === 4).length,
                3: user.ratings.filter(r => r.rating === 3).length,
                2: user.ratings.filter(r => r.rating === 2).length,
                1: user.ratings.filter(r => r.rating === 1).length
            }
        });
    } catch (error) {
        res.status(500).json({ message: "Error fetching user stats" });
    }
};

export const getRatingHistory = async (req, res) => {
    const userId = req.userId;
    try {
        const user = await User.findById(userId)
            .select('ratings')
            .populate({
                path: 'ratings.fromUserId',
                select: 'userName'
            });

        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }
        const sortedRatings = user.ratings.sort((a, b) => b.createdAt - a.createdAt);

        res.json({
            ratings: sortedRatings
        });
    } catch (error) {
        res.status(500).json({ message: "Error fetching rating history" });
    }
};
