package sample;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionHandler {

    final static String PUBLIC_KEY_FILE = "../common/keys/public_key.der";
    final static String PRIVATE_KEY_FILE = "../common/keys/private_key.der";
    private static PublicKey publicKey;
    private static PrivateKey privateKey;
    public static final String ALGORITHM = "RSA";


    public static void fetchPrivateKey()
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKey = kf.generatePrivate(spec);
    }

    public static void fetchPublicKey()
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publicKey = kf.generatePublic(spec);
    }

    public static String encrypt(String text) {
        try {
            if (publicKey == null)
                fetchPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String returnValue = DatatypeConverter.printBase64Binary(cipherText);
        System.out.println("Encrypted = " + returnValue);
        return returnValue ;
    }

    public static String decrypt(byte[] text) {
        try {
            if (privateKey == null)
                fetchPrivateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }

}
