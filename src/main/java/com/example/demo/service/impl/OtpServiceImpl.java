package com.example.demo.service.impl;

import com.example.demo.dto.request.SendOtpRequestDTO;
import com.example.demo.service.OtpService;
import com.example.demo.util.GenerateOTP;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.demo.util.SendEmailOtp;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final SendEmailOtp sendEmailOtp;

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateOtp(SendOtpRequestDTO sendOtpRequestDTO) {
        int result = sendEmailOtp.SendOtpEmail(sendOtpRequestDTO.getEmail());
        if (result == 0) {
            return "success";
        } else {
            return "failed";
        }
    }
}
