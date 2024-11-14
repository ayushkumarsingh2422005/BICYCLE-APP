import express from "express";
import { authenticateToken } from "../middleware/authMiddleware.js";
import { requestRide,ridesNotification,acceptRide, endRide, getRideDetails, getUserRides, getAllActiveRides, declineRide } from "../controllers/rideController.js";

const router = express.Router();

router.post("/request",authenticateToken, requestRide);
router.get("/notifications",authenticateToken, ridesNotification);
router.post("/accept",authenticateToken, acceptRide);
router.post("/decline",authenticateToken, declineRide);
router.post("/end",authenticateToken, endRide);
router.get("/active", getAllActiveRides);
router.get("/user-rides",authenticateToken, getUserRides);
router.get("/:rideId", getRideDetails);

export default router;
