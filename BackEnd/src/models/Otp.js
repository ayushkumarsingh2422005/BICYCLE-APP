import mongoose, {  Schema } from "mongoose";

const OtpSchema = new Schema({
  email: { type: String, required: true },
  otp: { type: Number, required: true },
  expiresAt: { type: Date, required: true },
});

const Otp = mongoose.model("Otp", OtpSchema);
export default Otp;
