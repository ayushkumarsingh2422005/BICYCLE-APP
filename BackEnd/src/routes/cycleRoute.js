import express from "express";
import { authenticateToken } from "../middleware/authMiddleware.js";
import { 
    addCycle, 
    updateCycle, 
    deleteCycle, 
    getCycleDetails,
    getAllAvailableCycles, 
    getUserCycles
} from "../controllers/cycleController.js";

const router = express.Router();

router.post("/add", authenticateToken, addCycle);
router.get("/available", getAllAvailableCycles);
router.get("/all",authenticateToken, getUserCycles);
router.put("/:cycleId", authenticateToken, updateCycle);
router.delete("/:cycleId", authenticateToken, deleteCycle);
router.get("/:cycleId", getCycleDetails);

export default router;
