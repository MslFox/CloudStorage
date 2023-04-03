package com.mslfox.cloudStorageServices.util;
import java.util.Base64;

public class Base64Util {

    public static String encode(String originalString) {
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }
    // may be useful for future
    public static String decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
