package bftsmart.demo.adapt.messages.sensor;

public class SignaturesMessage implements SensorMessage {
    private final int useSignatures;

    public SignaturesMessage(int useSignatures) {
        this.useSignatures = useSignatures;
    }

    public int getUseSignatures() {
        return useSignatures;
    }
}
