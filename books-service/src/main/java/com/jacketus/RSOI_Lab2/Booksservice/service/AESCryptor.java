package com.jacketus.RSOI_Lab2.Booksservice.service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
//import javax.xml.bind.DatatypeConverter;

public class AESCryptor {

    public static String encrypt(byte[] key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] v = value.getBytes("UTF-8");
            int pad = v.length - (v.length % 16) + 16;
            v = Arrays.copyOf(v, pad);
            //byte[] v = Arrays.copyOf(value.getBytes("UTF-8"), 16);
            byte[] encrypted = cipher.doFinal(v);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(byte[] key, String initVector, String encrypted) {
        try {

            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(encryptedBytes);

            return new String(original, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static byte[] decryptWOUTF8(byte[] key, String initVector, String encrypted) {
        try {

            byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(encryptedBytes);

            return original;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String encryptWOUTF8(byte[] key, String initVector, byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            //v = Arrays.copyOf(value, 16);
            byte[] v = value;
            int pad = v.length - (v.length % 16) + 16;
            v = Arrays.copyOf(v, pad);
            byte[] encrypted = cipher.doFinal(value);

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main() {
        byte[] key = new String("Hello12345617234").getBytes();
        String s = "RandomInitVector";
        System.out.println(decrypt(key, s, encrypt(key, s, "HelloWorld!")));
    }
}
