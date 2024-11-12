import express from "express";
import { startRide, endRide, getRideDetails, getUserRides, getAllActiveRides } from "../controllers/rideController.js";

const router = express.Router();

router.post("/start", startRide);
router.post("/end", endRide);
router.get("/active", getAllActiveRides);
router.get("/:rideId", getRideDetails);
router.get("/user/:userId", getUserRides);

export default router;
