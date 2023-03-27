package com.mslfox.cloudStorageServices.util;

import java.util.Base64;

public class StringEncoder {

    public static String originalStringToBase64String(String originalString) {
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }
    // may be useful for future
    public static String Base64StringToDecodeString(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}
