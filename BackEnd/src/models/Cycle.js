import mongoose, { Schema } from "mongoose";

const CycleSchema = new Schema(
    {
        ownerId: { type: Schema.Types.ObjectId, ref: "User", required: true },
        hourlyRate: { type: Number, required: true, default: 10 },
        model: { type: String, required: true },
        isAvailable: { type: Boolean, default: true }
    },
    { timestamps: true }
);

const Cycle = mongoose.model("Cycle", CycleSchema);
export default Cycle;
