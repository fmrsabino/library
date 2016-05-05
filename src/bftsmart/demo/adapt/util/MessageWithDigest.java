package bftsmart.demo.adapt.util;

import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class MessageWithDigest<T> implements Serializable {
    public static final String HASH_FUNCTION = "SHA-256";
    public static final String KEY_FUNCTION = "RSA";

    private T content;
    private byte[] digest;

    public MessageWithDigest(T content, PrivateKey key) throws IOException {
        this.content = content;
        byte[] unsignedDigest = SecurityUtils.hash(MessageSerializer.serialize(content), HASH_FUNCTION);
        this.digest = SecurityUtils.encrypt(unsignedDigest, key, KEY_FUNCTION);
    }

    public boolean isValid(PublicKey key) {
        try {
            byte[] hashedContent = SecurityUtils.hash(MessageSerializer.serialize(content), HASH_FUNCTION);
            byte[] decipheredHash = SecurityUtils.decrypt(digest, key, KEY_FUNCTION);
            return Arrays.equals(hashedContent, decipheredHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public T getContent() {
        return content;
    }
}
