package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SecurityUtils {
    public static PrivateKey getPrivateKey(String filePath, String algorithm) {
        try {
            String contents = FileUtil.readAllBytes(filePath);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(contents));
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(String filePath, String algorithm) {
        try {
            String contents = FileUtil.readAllBytes(filePath);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(contents));
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] text, Key key, String algorithm) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] encrypt(byte[] content, Key key, String algorithm) {
        byte[] result = null;
        try {
            final Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] hash(byte[] contents, String algorithm) {
        byte[] result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            result = messageDigest.digest(contents);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            PrivateKey privateKey = getPrivateKey("rsa/privatekey0", "RSA");
            PublicKey publicKey = getPublicKey("rsa/publickey0", "RSA");
            BandwidthMessage bandwidthMessage = new BandwidthMessage(0, 0, 100);
            MessageWithDigest<BandwidthMessage> msg = new MessageWithDigest<>(bandwidthMessage, privateKey);
            if (msg.isValid(publicKey)) {
                System.out.println("Message is VALID! Value is: " + msg.getContent().getBandwidth());
            } else {
                System.out.println("Message is NOT valid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
