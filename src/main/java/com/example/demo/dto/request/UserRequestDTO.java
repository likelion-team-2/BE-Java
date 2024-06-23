package com.example.demo.dto.request;

import com.example.demo.util.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class UserRequestDTO implements Serializable {

    @NotBlank(message = "Password must not be blank")
    private String password;
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Email invalid format")
    private String email;

    @NotNull(message = "Birthdate not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate birthDate;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be numeric")
    @PhoneNumber(message = "phone invalid format")
    private String phoneNumber;

    @NotNull(message = "Telegram not be null")
    private String telegram;
    @NotNull(message = "Address not be null")
    private String address;
    @NotNull (message = "Sex not be null")
    private String sex;

    public UserRequestDTO(String password, String name, String email, LocalDate birthDate, String phoneNumber, String telegram, String address, String sex) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.telegram = telegram;
        this.address = address;
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTelegram() {
        return telegram;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }
}
