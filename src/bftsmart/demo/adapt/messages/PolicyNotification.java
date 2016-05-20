package bftsmart.demo.adapt.messages;

public class PolicyNotification extends Message {
    public PolicyNotification(int seqN, int sender) {
        super(seqN, sender);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
