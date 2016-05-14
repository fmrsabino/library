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
