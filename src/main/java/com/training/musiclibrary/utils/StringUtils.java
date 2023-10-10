package com.training.musiclibrary.utils;

public class StringUtils {
    private StringUtils(){}
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }
}
