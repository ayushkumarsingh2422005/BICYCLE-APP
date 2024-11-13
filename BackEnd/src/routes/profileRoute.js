import express from "express";
import { getUserData } from "../controllers/userController.js";
import {authenticateToken} from "../middleware/authMiddleware.js";

const router = express.Router();

router.get("/data",authenticateToken, getUserData);

export default router;