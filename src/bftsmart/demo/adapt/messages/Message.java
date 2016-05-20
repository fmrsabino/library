package bftsmart.demo.adapt.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final int seqN;
    private final int sender;

    public Message(int seqN, int sender) {
        this.seqN = seqN;
        this.sender = sender;
    }

    public int getSeqN() {
        return seqN;
    }

    public int getSender() {
        return sender;
    }
}
