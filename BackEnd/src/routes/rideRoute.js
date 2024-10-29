import { Router } from "express";
import { authenticateToken } from "../middleware/authMiddleware.js";
import { riderName } from "../controllers/rideController.js";

const router = Router();

router.get("/owner-info", authenticateToken, riderName);

export default router;
