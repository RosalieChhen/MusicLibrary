package com.training.musiclibrary.utils;

public class EnumUtils {
    private EnumUtils() {}

    public static boolean isNullOrEmpty(Enum e) {
        return e == null || e.name() == "";
    }
}
