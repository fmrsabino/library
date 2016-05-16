package bftsmart.demo.adapt.messages.adapt;

import java.io.Serializable;

public abstract class AdaptMessage implements Serializable {
    private final int seqN;
    private final int sender;

    public AdaptMessage(int seqN, int sender) {
        this.seqN = seqN;
        this.sender = sender;
    }

    //executes the reconfiguration
    public abstract void execute();

    public int getSeqN() {
        return seqN;
    }

    public int getSender() {
        return sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdaptMessage that = (AdaptMessage) o;

        if (seqN != that.seqN) return false;
        return sender == that.sender;

    }

    @Override
    public int hashCode() {
        int result = seqN;
        result = 31 * result + sender;
        return result;
    }
}
