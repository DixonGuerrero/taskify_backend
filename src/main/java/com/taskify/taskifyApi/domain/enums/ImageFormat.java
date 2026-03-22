package com.taskify.taskifyApi.domain.enums;

import java.util.Arrays;

public enum ImageFormat {
    PNG,
    JPEG,
    JPG,
    WEBP;


    public static boolean isValid(String format) {
        return Arrays.stream(values()).anyMatch(f -> f.name().equals(format));
    }


}
