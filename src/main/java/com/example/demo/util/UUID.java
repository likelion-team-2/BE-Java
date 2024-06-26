package com.example.demo.util;

public class UUID {
public static String GenerateUUID() {
    String UUID = java.util.UUID.randomUUID().toString();
        return UUID;
    }
}
