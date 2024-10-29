import express from "express";
import connectDB from "./db.js";
import authRoute from "./routes/authRoute.js";
import rideRoute from "./routes/rideRoute.js";
import swaggerUi from "swagger-ui-express";
import swaggerSpec from "../swaggerConfig.js";

const app = express();
app.use(express.json());

connectDB();

app.get("/",(req,res)=>{
    res.send("✅ Server is running ");
});
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));
app.use("/api/auth", authRoute);
app.use("/api/ride", rideRoute);

app.use((err, req, res, next)=>{
    res.status(500).json({message: err.message});
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server started on port ${PORT}`);
});