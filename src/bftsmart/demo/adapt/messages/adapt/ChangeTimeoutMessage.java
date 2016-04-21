package bftsmart.demo.adapt.messages.adapt;

public class ChangeTimeoutMessage implements AdaptMessage {
    private final long timeoutValue;

    public ChangeTimeoutMessage(long timeoutValue) {
        this.timeoutValue = timeoutValue;
    }

    public long getTimeoutValue() {
        return timeoutValue;
    }

    @Override
    public int hashCode() {
        return (int) timeoutValue;
    }
}
