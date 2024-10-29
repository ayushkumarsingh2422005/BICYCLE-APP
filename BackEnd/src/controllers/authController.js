import bcrypt from "bcryptjs";
import jwt from "jsonwebtoken";
import User from "../models/User.js";
import Otp from "../models/Otp.js";
import { sendPasswordResetOTP } from "../services/emailServices.js";

export const register = async (req, res) => {
    const { username, email, password } = req.body;

    try {
        const existingEmailUser = await User.findOne({ email });
        if (existingEmailUser) {
            return res.status(400).json({ message: "Email already in use" });
        }

        const existingUsernameUser = await User.findOne({ userName: username });
        if (existingUsernameUser) {
            return res.status(400).json({ message: "Username already in use" });
        }


        const hashedPassword = await bcrypt.hash(password, 10);
        const newUser = new User({ email, userName: username, password: hashedPassword });
        await newUser.save();

        res.status(201).json({ message: "User registered successfully" });
    } catch (error) {
        console.error("Registration error:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const login = async (req, res) => {
    const { email, password } = req.body;
    try {
        const existingUser = await User.findOne({ email });
        if (!existingUser) {
            res.status(400).json({ message: "Invalid credentials" });
            return;
        }
        const passwordMatch = await bcrypt.compare(password, existingUser.password);
        if (!passwordMatch) {
            res.status(400).json({ message: "Invalid credentials" });
            return;
        }
        const token = jwt.sign({ id: existingUser._id }, process.env.JWT_SECRET, { expiresIn: '7d' });
        res.status(200).json({ token: token });
    } catch (error) {
        res.status(500).json({ message: "Server error" });
    }
};

export const resetPassword = async (req, res) => {
    const { email, password } = req.body;
    try {
        const existingUser = await User.findOne({ email });
        if (!existingUser) {
            res.status(404).json({ message: "User Not Found" });
            return;
        }

        const hashedPassword = await bcrypt.hash(password, 10);
        existingUser.password = hashedPassword;
        await existingUser.save();
        res.status(200).json({ message: "Password reset successfully" });
    } catch (error) {
        res.status(500).json({ message: "Server error" });
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
            res.status(404).json({ message: "User not found" });
            return;
        }

        await Otp.deleteMany({ email });
        const newOtp = new Otp({ email, otp, expiresAt: otpExpiry });
        await newOtp.save();
        await sendPasswordResetOTP(email, otp);
        res.status(200).json({ message: "OTP sent successfully" });

    } catch (error) {
        console.error("Error sending OTP:", error);
        res.status(500).json({ message: "Server error" });
    }
};

export const verifyOtp = async (req, res) => {
    const { email, otp } = req.body;

    try {
        const otpRecord = await Otp.findOne({ email, otp });

        if (!otpRecord) {
            res.status(400).json({ message: "Invalid OTP" });
            return;
        }

        if (otpRecord.expiresAt < new Date()) {
            await Otp.deleteMany({ email });
            res.status(400).json({ message: "OTP has expired" });
            return;
        }

        await Otp.deleteMany({ email });
        res.status(200).json({ message: "OTP verified successfully" });
    } catch (error) {
        console.error("Error verifying OTP:", error);
        res.status(500).json({ message: "Server error" });
    }
};
