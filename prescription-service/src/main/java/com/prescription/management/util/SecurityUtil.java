package com.prescription.management.util;

import com.prescription.commons.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class SecurityUtil {

    public static final String BASE64_PRIVATE_KEY = "MIICXgIBAAKBgQC+e0r5uXf9SccvmsBBGniEC1EKHYPrpzYA3QgaASbVgHTgcPNLXf5yBdxOhWF9Z5Zj47aAyhJxILD6HFhq3zOHkDrdV9ReIVg07Ez719YS8Lyi9ZachcroEyZmoNttGmzsZk2UX75oSzkDuDb+k7psxCvNDM2vubo8tpRooaoLLwIDAQABAoGAUrff+0pbnRbo9PdS/fKs20q+ypG+tBtNDFrwJbrSTm0WVFE4lkYfNQzuevrZN0hAuugsXQljsNTthe/BRcff7AKE1OUyzpKnAqKc00HQAzuZRAAY2Q4qu2mnnsmxw32amBaUg+F6PBC+xMIhKevc0kWjeGnIFS+qP8VXZUFPTsECQQDvDr+x6329Gjz6o0LDVpfP85I5XBf9m5meP6FZN7KkdybLMy9L2HQFs1ctrtKOnIMyTvowVKJUvjMIDpUnSnZHAkEAy/s5THcb2ppj8TxvFK83qyARxDVl6+yVsbeGRDdqu+xLOeox7ogahx/xr+8lrZ+/BNcMYetfAAdgbxjRfiVv2QJBAL9NYS/jEJssjRHRZlLlT9V0yFYokXY8d4FA0ECSJt4gD6ZajoW2cJpDOwduuEaxjvfKZcIZaFJCZpoS+V8Y4m8CQQCefiN1YI8J+jBxvs1ouwR8OLzVQdBh0kA2CC2RT6XlQqLo6ynE94f1OkLmuuEB2T/pGNcMNoBoW1L4v8X3SEtxAkEAhktkgG862HybPSjqq5se4vRY0yFvuRiojafQofiG9h8xJ6KZr2QyKe++VZ7rQMw1U3zVwKoBWqO66Lwz6tsVlg==";

    public String getRsaDecryptedData(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(BASE64_PRIVATE_KEY));
            final byte[] inputBytes = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(inputBytes);
        } catch (final Exception e) {
            log.error("Decryption failed", e);
            throw new ApiException("Decryption failed", e);
        }
    }

    private PrivateKey getRsaPrivateKey(final String base64PrivateKey) {
        try {
            final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (final NoSuchAlgorithmException e) {
            log.error("No such algorithm", e);
            throw new ApiException("No such algorithm", e);
        } catch (final InvalidKeySpecException e) {
            log.error("Invalid key spec", e);
            throw new ApiException("Invalid key spec", e);
        }
    }

    public String getAesDecryptedData(final String input, final String aesDecryptedKey) {
        try {
            final SecretKey secretKey = generateKey(128);
            final IvParameterSpec iv = generateIv();
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            final byte[] outputBytes = cipher.doFinal(Base64.getDecoder().decode(input));
            return Base64.getEncoder().encodeToString(outputBytes);
        } catch (final NoSuchAlgorithmException e) {
            log.error("", e);
            throw new ApiException("", e);
        } catch (final NoSuchPaddingException e) {
            log.error("", e);
            throw new ApiException("", e);
        } catch (final InvalidAlgorithmParameterException e) {
            log.error("", e);
            throw new ApiException("", e);
        } catch (final InvalidKeyException e) {
            log.error("", e);
            throw new ApiException("", e);
        } catch (final IllegalBlockSizeException e) {
            log.error("", e);
            throw new ApiException("", e);
        } catch (final BadPaddingException e) {
            log.error("", e);
            throw new ApiException("", e);
        }
    }

    private IvParameterSpec generateIv() {
        final byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private SecretKey generateKey(final int n) throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    private SecretKey getKeyFromPassword(final String password, final String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        final KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
    }
}
