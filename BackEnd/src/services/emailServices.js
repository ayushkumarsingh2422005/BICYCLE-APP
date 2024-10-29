import nodemailer from 'nodemailer';
import dotenv from 'dotenv';

dotenv.config();

const transporter = nodemailer.createTransport({
  service: 'Gmail',
  host: 'smtp.gmail.com',
  port: 465,
  secure:true,
  auth: {
    user: process.env.EMAIL,       
    pass: process.env.PASSWORD, 
  },
});

export const sendPasswordResetOTP = async (email, OTP) => {
  try {
    await transporter.sendMail({
      from: `"Pedaller" <${process.env.EMAIL}>`,
      to: email,
      subject: 'Password Reset Request',
      text: `Your One Time Password to reset your password is : ${OTP}`,
    });
    console.log('Email sent successfully');
  } catch (error) {
    console.error('Error sending email:', error);
  }
};
