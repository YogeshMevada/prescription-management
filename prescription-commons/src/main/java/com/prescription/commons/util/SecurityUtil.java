package com.prescription.commons.util;

import com.prescription.commons.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SecurityUtil {

    public static final String BASE64_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKE6/rExRX8orXQT+g5CyJ4Y2eb8iZi3c8Evs+anzPhAj+I4stYBWpQ+rjpeDRbnGwr/GR51ZbNuEFx8gh4w57c0nbf1iRgXsNcTfUXf69Ly7m1fYEPz+h+Piev1OV4kTdf2RfaYevEdzeTE9fcYoYNGLiCU5HWEdH6nKHdI2IY/AgMBAAECgYBoyZDy1NUYOfXRtZIVZq5AomIs0AEVslWXP0urI3ACAOQC4VrJ4ANgygQuhyX2tqlK0lUfCKnanjQgCAQBX0OZNIypMvofLwanSm2wnQpP7bZcG4JoX9Pizb4HYfpX+fNb6g/4Yit1Sj0QndV3E6m4LX+DNTNEGPjUHyMtUC5SwQJBAM2pjMx1kcMEENFUlxwE/YviTaNwUJSJZ2oRAaN32ArcqF93oYHdt7h3oRDaO9Z7/p9PAWcxvIa/6aQpDOFHYtECQQDIsWug4FpE0iTpxDio4tlYWDbySnEFej9tUE2rJE7vh96M8BhfQTwaLs7iGSEsVi2RdwhDYVYSuUBgMlf7JPwPAkEAgad2IB+ueOdDQvS1HvD2dw2ALJ1N75aAH5oBEnEnPhHGBAmDRD/m5uBkJh+uwZUB/KTuGYR9eeXbAabve8ZpQQJAa+vMfsRmhl+i7fpQpbDnQ7ScR4p0YIeul+/49DEgKXqc2Jk74e1IjqPhKIBu8FKfSnwB+4naKVNpJNGtsiXfTQJATJiCXH9icH7CWMVfAjlLnVAkjeBd+EO+ix+JkMgyHmidvfGiEKZnZpQA/OsX0IC8Xp5DorIZ1OkirLq4Lw14Mg==";
    public static final String BASE64_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChOv6xMUV/KK10E/oOQsieGNnm/ImYt3PBL7Pmp8z4QI/iOLLWAVqUPq46Xg0W5xsK/xkedWWzbhBcfIIeMOe3NJ239YkYF7DXE31F3+vS8u5tX2BD8/ofj4nr9TleJE3X9kX2mHrxHc3kxPX3GKGDRi4glOR1hHR+pyh3SNiGPwIDAQAB";

    public Map<String, String> generateKeyPairs() throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final PrivateKey privateKey = keyPair.getPrivate();
        final PublicKey publicKey = keyPair.getPublic();
        final HashMap<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("PRIVATE_KEY", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        keyPairMap.put("PUBLIC_KEY", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return keyPairMap;
    }

    public String getRsaEncryptData(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(BASE64_PUBLIC_KEY));
            return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
        } catch (final Exception e) {
            log.error("Encryption failed", e);
            throw new ApiException("Encryption failed", e);
        }
    }

    private PublicKey getRsaPublicKey(final String base64PublicKey) {
        try {
            final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (final Exception e) {
            log.error("Encryption failed", e);
            throw new ApiException("Encryption failed", e);
        }
    }

    public String getRsaDecryptData(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(BASE64_PRIVATE_KEY));
            return new String(cipher.doFinal(Base64.getDecoder().decode(input.getBytes())));
        } catch (final Exception e) {
            log.error("Decryption failed", e);
            throw new ApiException("Decryption failed", e);
        }
    }

    private PrivateKey getRsaPrivateKey(final String base64PrivateKey) {
        try {
            final byte[] decodedPrivateKey = Base64.getDecoder().decode(base64PrivateKey.getBytes());
            final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
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

    public static String getAesEncryptData(final String input, final String secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getAesSecretKey(secret));
        return Base64.getEncoder().encodeToString(cipher.doFinal(input.getBytes()));
    }

    public static String getAesDecryptData(final String input, final String secret) {
        try {
            final byte[] cipherData = Base64.getDecoder().decode(input.getBytes());
            byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
            byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(), MessageDigest.getInstance("MD5"));
            final SecretKeySpec secretKeySpec = new SecretKeySpec(keyAndIV[0], "AES");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(keyAndIV[1]);
            byte[] encryptedData = Arrays.copyOfRange(cipherData, 16, cipherData.length);
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(encryptedData));
        } catch (final NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                       BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.error("" +
                    "", e);
            throw new ApiException("", e);
        }
    }

    private static SecretKeySpec getAesSecretKey(final String secret) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        final byte[] digest = md.digest(secret.getBytes());
        return new SecretKeySpec(Arrays.copyOf(digest, 16), "AES");
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

    public static byte[][] generateKeyAndIV(final int keyLength, final int ivLength, final int iterations, final byte[] salt, final byte[] password, final MessageDigest md) {
        int digestLength = md.getDigestLength();
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        byte[] generatedData = new byte[requiredLength];
        int generatedLength = 0;

        try {
            md.reset();

            // Repeat process until sufficient data has been generated
            while (generatedLength < keyLength + ivLength) {

                // Digest data (last digest if available, password data, salt if available)
                if (generatedLength > 0)
                    md.update(generatedData, generatedLength - digestLength, digestLength);
                md.update(password);
                if (salt != null)
                    md.update(salt, 0, 8);
                md.digest(generatedData, generatedLength, digestLength);

                // additional rounds
                for (int i = 1; i < iterations; i++) {
                    md.update(generatedData, generatedLength, digestLength);
                    md.digest(generatedData, generatedLength, digestLength);
                }

                generatedLength += digestLength;
            }

            // Copy key and IV into separate byte arrays
            byte[][] result = new byte[2][];
            result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
            if (ivLength > 0)
                result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

            return result;

        } catch (DigestException e) {
            throw new RuntimeException(e);

        } finally {
            // Clean out temporary data
            Arrays.fill(generatedData, (byte) 0);
        }
    }
}
