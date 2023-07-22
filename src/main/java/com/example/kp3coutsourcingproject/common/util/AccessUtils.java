package com.example.kp3coutsourcingproject.common.util;

public abstract class AccessUtils {

    public static boolean getSafe(Boolean value, boolean b) {
        return (value == null) ? b : value;
    }
}
