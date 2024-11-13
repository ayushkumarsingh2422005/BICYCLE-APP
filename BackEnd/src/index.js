import express from "express";
import connectDB from "./db.js";
import authRoute from "./routes/authRoute.js";
import rideRoute from "./routes/rideRoute.js";
import cycleRoute from "./routes/cycleRoute.js";
import profileRoute from "./routes/profileRoute.js";
import cors from "cors";

const app = express();
app.use(express.json());
app.use(cors());

connectDB();

app.get("/",(req,res)=>{
    res.send("✅ Server is running ");
});

app.use("/api/auth", authRoute);
app.use("/api/ride", rideRoute);
app.use("/api/cycles", cycleRoute);
app.use("/api/profile", profileRoute);

app.use((err, req, res, next) => {
    res.status(err.status || 500).json({ message: err.message || "Internal Server Error" });
});

app.use((err, req, res, next)=>{
    res.status(500).json({message: err.message});
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server started on port ${PORT}`);
});