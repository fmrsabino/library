package bftsmart.demo.adapt.messages;

import java.io.Serializable;

public class TestMessage implements AdaptMessage, Serializable {
    private int value = -1000;

    public TestMessage(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
