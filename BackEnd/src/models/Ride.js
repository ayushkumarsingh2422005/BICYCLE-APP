import mongoose, { Schema } from "mongoose";

const RideSchema = new Schema(
    {
        userId: { type: Schema.Types.ObjectId, ref: "User", required: true },
        cycleId: { type: Schema.Types.ObjectId, ref: "Cycle", required: true },
        startLocation: {
            x: { type: String, required: false },
            y: { type: String, required: false }
        },
        endLocation: {
            x: { type: String, required: false },
            y: { type: String, required: false }
        },
        startTime: { type: Date },
        endTime: { type: Date },
        cost: { type: Number, required: false },
        isActive: { type: Boolean, default: false }
    },
    { timestamps: true }
);

const Ride = mongoose.model("Ride", RideSchema);
export default Ride;