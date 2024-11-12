import mongoose from "mongoose";
import dotenv from "dotenv";

dotenv.config();

const connectDB = async () => {
    const mongodbUri = process.env.MONGODB_URI;

    if (!mongodbUri) {
        console.error("MongoDB URI not defined in .env file");
        process.exit(1);
    }

    try {
        await mongoose.connect(mongodbUri);
        console.log("MongoDB connected");
    } catch (error) {
        console.error("MongoDB connection failed: ", error.message);
        process.exit(1);
    }
};

process.on('SIGINT', async () => {
    console.log("Closing MongoDB connection...");
    await mongoose.connection.close();
    console.log("MongoDB connection closed");
    process.exit(0);
});

export default connectDB;
