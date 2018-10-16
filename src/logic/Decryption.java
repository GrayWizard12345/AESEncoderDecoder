package logic;

import view.Starter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

public class Decryption {


    private byte[] file;
    private SecureRandom random;
    private Cipher cipher;
    private static int keySize = 256;
    private KeyGenerator keyGenerator;
    private SecretKeySpec secretKeySpec;
    private SecretKey secretKey;
    private byte[] iv;
    private byte[] plainText;

    private byte[] key;

    public Decryption(byte[] file, byte[] key, byte[] iv) {
        this.file = file;
        this.iv = iv;
        this.key = key;
        random = new SecureRandom();
    }

    public String decipher() throws  InvalidAlgorithmParameterException {
        try {
            secretKey = new SecretKeySpec(key, 0, key.length, "AES");
            Starter.getDecryptionDetails().setText(Starter.getDecryptionDetailsText().concat("\nSecretKey format:\t\t" + secretKey.getFormat()));

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            System.out.println("Cipher during dec:" + file.length);
            plainText = cipher.doFinal(file);

            return new String(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }



    public byte[] getPlainText() {
        return plainText;
    }

}
