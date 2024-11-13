import express from "express";
import { authenticateToken } from "../middleware/authMiddleware.js";
import { 
    updateProfile, 
    getProfileDetails,
    getUserStats,
    getRatingHistory 
} from "../controllers/userController.js";

const router = express.Router();

router.get("/details", authenticateToken, getProfileDetails);
router.put("/update", authenticateToken, updateProfile);
router.get("/stats", authenticateToken, getUserStats);
router.get("/ratings", authenticateToken, getRatingHistory);

export default router;
