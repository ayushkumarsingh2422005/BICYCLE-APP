import mongoose, { Schema } from "mongoose";

const RideSchema = new Schema(
    {
        userId: { type: Schema.Types.ObjectId, ref: "User", required: true },
        cycleId: { type: Schema.Types.ObjectId, ref: "Cycle", required: true },
        startLocation: {
            x: { type: String, required: true },
            y: { type: String, required: true }
        },
        endLocation: {
            x: { type: String, required: false },
            y: { type: String, required: false }
        },
        startTime: { type: Date, required: true, default: Date.now },
        endTime: { type: Date },
        cost: { type: Number, required: false },
        isActive: { type: Boolean, default: true }
    },
    { timestamps: true }
);

const Ride = mongoose.model("Ride", RideSchema);
export default Ride;