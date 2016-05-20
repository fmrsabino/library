package bftsmart.demo.adapt.util;

import java.io.*;

public class MessageSerializer {
    public static <T> byte[] serialize(T message) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(message);
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(in);
        return (T) ois.readObject();
    }
}
