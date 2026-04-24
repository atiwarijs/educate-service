package com.eduservices.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


@Component
public class SyncEncryption {

    private static final Logger log = LoggerFactory.getLogger(SyncEncryption.class);

    @Value("${sync.encryption.key}")
    String key;
    @Value("${sync.encryption.vector}")
    String initVector;
    @Value("${sync.encryption.algo}")
    String algo;

    public String encrypt(String value) {
        try {
            Cipher cipher = createCipher(Cipher.ENCRYPT_MODE);
            assert cipher != null;
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            log.error("Error in encrypt: {} ",ex.getLocalizedMessage());
        }
        return null;
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = createCipher(Cipher.DECRYPT_MODE);
            assert cipher != null;
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            log.error("Error in decrypt: {} ",ex.getLocalizedMessage());
        }
        return null;
    }

    private Cipher createCipher(int mode){
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(mode, keySpec, iv);
            return cipher;
        } catch (Exception ex) {
            log.error("Error in createCipher: {} ",ex.getLocalizedMessage());
        }
        return null;
    }
}
