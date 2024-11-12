import express from "express";
import { login, register, resetPassword, sendOtp, verifyOtp } from "../controllers/authController.js";

const router = express.Router();

router.post("/login", login);
router.post("/register", register);
router.post("/reset-password", resetPassword);
router.post("/send-otp", sendOtp);
router.post("/verify-otp", verifyOtp);

export default router;
