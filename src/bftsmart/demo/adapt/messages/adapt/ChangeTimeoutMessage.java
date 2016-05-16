package bftsmart.demo.adapt.messages.adapt;

public class ChangeTimeoutMessage extends AdaptMessage {
    private final long timeoutValue;

    public ChangeTimeoutMessage(int seqN, int sender, long timeoutValue) {
        super(seqN, sender);
        this.timeoutValue = timeoutValue;
    }

    public long getTimeoutValue() {
        return timeoutValue;
    }

    @Override
    public int hashCode() {
        return (int) (timeoutValue ^ (timeoutValue >>> 32));
    }

    @Override
    public String toString() {
        return "ChangeTimeoutMessage{" +
                "timeoutValue=" + timeoutValue +
                '}';
    }

    @Override
    public void execute() {

    }
}
