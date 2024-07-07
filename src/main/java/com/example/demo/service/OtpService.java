package com.example.demo.service;

import com.example.demo.dto.request.SendOtpRequestDTO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface OtpService {

    String generateOtp(SendOtpRequestDTO sendOtpRequestDTO) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
