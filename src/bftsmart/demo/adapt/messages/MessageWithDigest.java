package bftsmart.demo.adapt.messages;

import bftsmart.demo.adapt.util.MessageSerializer;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class MessageWithDigest<T> implements Serializable {
    private T content;
    private byte[] digest;

    public MessageWithDigest(T content) {
        this.content = content;
    }

    public void sign(PrivateKey key) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(key);
            signature.update(MessageSerializer.serialize(content));
            digest = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verify(PublicKey key) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(key);
            signature.update(MessageSerializer.serialize(content));
            return signature.verify(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public T getContent() {
        return content;
    }
}
