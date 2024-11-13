import mongoose, { Schema } from "mongoose";

const UserSchema = new Schema(
    {
        userName: { type: String, required: true },
        firstName: { type: String, required: false },
        lastName: { type: String, required: false },
        regNumber: { type: String, required: false },
        phoneNumber: {
            type: String,
            required: false,
            unique: true,
            match: [/^\d{10}$/, 'Please enter a valid 10-digit phone number']
        },
        hostel: { type: String, required: false },
        roomNumber: { type: String, required: false },
        currentLocation: {
            type: { type: String, default: "Point" },
            coordinates: { type: [Number], required: false }
        },
        email: {
            type: String,
            required: true,
            unique: true,
            match: [/^\S+@\S+\.\S+$/, 'Please use a valid email address']
        },
        password: { type: String, required: true },
        isDeleted: { type: Boolean, default: false },
        role: { type: String, enum: ["owner", "user"], default: "user" },
        isProfileComplete: { type: Boolean, default: false },
        isVerified: { type: Boolean, default: false },
        ratings: [
            {
                rating: { type: Number, min: 1, max: 5 },
                comment: String,
                fromUserId: { type: Schema.Types.ObjectId, ref: "User" }
            }
        ]
    },
    { timestamps: true }
);

const User = mongoose.model("User", UserSchema);
export default User;