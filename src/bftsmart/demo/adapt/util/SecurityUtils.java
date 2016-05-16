package bftsmart.demo.adapt.util;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
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
}
