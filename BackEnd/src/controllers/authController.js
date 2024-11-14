import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import User from "../models/User.js";
import Otp from "../models/Otp.js";
import { sendPasswordResetOTP } from "../services/emailServices.js";

const generateToken = (id) => {
    return jwt.sign({ id }, process.env.JWT_SECRET, { expiresIn: '7d' });
};

export const register = async (req, res) => {
    const { username, email, password, phoneNumber } = req.body;

    try {
        const existingEmailUser = await User.findOne({ email });
        const existingUsernameUser = await User.findOne({ userName: username });

        if (existingEmailUser || existingUsernameUser) {
            return res.status(400).json({ message: "Email or Username already in use" });
        }

        const hashedPassword = await bcrypt.hash(password, 10);
        const newUser = new User({ email, userName: username, phoneNumber, password: hashedPassword });
        await newUser.save();

        const token = generateToken(newUser._id);
        res.status(201).json({ message: "User registered successfully", token });
    } catch (error) {
        console.error("Registration error:", error);
        res.status(500).json({ message: error.message || "Server error" });
    }
};

export const login = async (req, res) => {
    const { email, password } = req.body;

    try {
        const existingUser = await User.findOne({ email });
        if (!existingUser) {
            return res.status(400).json({ message: "Invalid credentials" });
        }

        const passwordMatch = await bcrypt.compare(password, existingUser.password);
        if (!passwordMatch) {
            return res.status(400).json({ message: "Invalid credentials" });
        }

        const token = generateToken(existingUser._id);
        res.status(200).json({ token });
    } catch (error) {
        console.error("Login error:", error);
        res.status(500).json({ message: error.message || "Server error" });
    }
};

export const resetPassword = async (req, res) => {
    const { email, password } = req.body;

    try {
        const existingUser = await User.findOne({ email });
        if (!existingUser) {
            return res.status(404).json({ message: "User not found" });
        }

        const hashedPassword = await bcrypt.hash(password, 10);
        existingUser.password = hashedPassword;
        await existingUser.save();

        res.status(200).json({ message: "Password reset successfully" });
    } catch (error) {
        console.error("Password reset error:", error);
        res.status(500).json({ message: error.message || "Server error" });
    }
};

const generateOTP = () => Math.floor(100000 + Math.random() * 900000);

export const sendOtp = async (req, res) => {
    const { email } = req.body;
    const otp = generateOTP();
    const otpExpiry = new Date(Date.now() + 10 * 60 * 1000);

    try {
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        await Otp.deleteMany({ email });
        const newOtp = new Otp({ email, otp, expiresAt: otpExpiry });
        await newOtp.save();

        await sendPasswordResetOTP(email, otp);
        res.status(200).json({ message: "OTP sent successfully" });
    } catch (error) {
        console.error("Error sending OTP:", error);
        res.status(500).json({ message: error.message || "Server error" });
    }
};

export const verifyOtp = async (req, res) => {
    const { email, otp } = req.body;

    try {
        const otpRecord = await Otp.findOne({ email, otp });

        if (!otpRecord) {
            return res.status(400).json({ message: "Invalid OTP" });
        }

        if (otpRecord.expiresAt < new Date()) {
            await Otp.deleteMany({ email });
            return res.status(400).json({ message: "OTP has expired" });
        }

        await Otp.deleteMany({ email, otp });
        res.status(200).json({ message: "OTP verified successfully" });
    } catch (error) {
        console.error("Error verifying OTP:", error);
        res.status(500).json({ message: error.message || "Server error" });
    }
};
