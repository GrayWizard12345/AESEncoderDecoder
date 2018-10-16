package logic;


import view.Starter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encrytion {

    private byte[] file;
    private SecureRandom random;
    private Cipher cipher;
    private static int keySize = 256;
    private KeyGenerator keyGenerator;
    private SecretKeySpec secretKeySpec;
    private SecretKey secretKey;
    private byte[] iv;
    private byte[] ciphertext;

    private byte[] key;

    public Encrytion(byte[] file) {
        this.file = file;
        random = new SecureRandom();
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(keySize,random);
            secretKey = keyGenerator.generateKey();
            secretKeySpec = new SecretKeySpec(key = secretKey.getEncoded(), "AES");

            Starter.getEncryptionDetails().setText(Starter.getEncryptionDetailsText().concat("\nSecretKey format:\t\t" + secretKey.getFormat()));

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ciphertext = cipher.doFinal(file);
            System.out.println("Cipher byte array length:" + ciphertext.length);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidParameterSpecException | InvalidKeyException e) {
            e.printStackTrace();
        }

    }



    public byte[] getCiphertext() {
        return ciphertext;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getKey() {
        return Base64.getEncoder().encodeToString(key);
    }

    public byte[] getKeyByteArray()
    {
        return key;
    }

}


