package com.example.demo.util;

public class GenerateOTP {
    public static String generateOTP() {
        int randomPin = (int) (Math.random() * 1000000); // Generates a number between 0 and 999999
        String otp = String.format("%06d", randomPin); // Formats the number to ensure it is always 6 digits
        return otp;
    }
}
