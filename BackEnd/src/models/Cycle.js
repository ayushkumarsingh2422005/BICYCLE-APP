import mongoose, { Schema } from "mongoose";

const CycleSchema = new Schema(
    {
        ownerId: { type: Schema.Types.ObjectId, ref: "User", required: true },
        hourlyRate: { type: Number, required: true, min: 0 },
        model: { type: String, required: true },
        location: { type: String, required: true },
        slot: { 
            type: String, 
            required: true,
            validate: {
                validator: function(v) {
                    return /^\d{2}:\d{2} - \d{2}:\d{2}$/.test(v);
                },
                message: props => `${props.value} is not a valid time slot! Format should be "HH:mm - HH:mm".`
            }
        },
        isAvailable: { type: Boolean, default: true }
    },
    { timestamps: true }
);

const Cycle = mongoose.model("Cycle", CycleSchema);
export default Cycle;
