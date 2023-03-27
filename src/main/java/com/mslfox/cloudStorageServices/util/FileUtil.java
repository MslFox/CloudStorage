package com.mslfox.cloudStorageServices.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class FileUtil {
    @Value("${file.storage.hash.algorithm}")
    private String algorithm;

    public String getHash(byte[] fileBytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        int offset = 0;
        while (offset < fileBytes.length) {
            int length = Math.min(8192, fileBytes.length - offset);
            md.update(fileBytes, offset, length);
            offset += length;
        }
        return bytesToHex(md.digest());
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}