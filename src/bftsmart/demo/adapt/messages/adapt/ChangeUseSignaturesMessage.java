package bftsmart.demo.adapt.messages.adapt;

public class ChangeUseSignaturesMessage implements AdaptMessage {
    private final int useSignatures;

    public ChangeUseSignaturesMessage(int useSignatures) {
        this.useSignatures = useSignatures;
    }

    public int getUseSignatures() {
        return useSignatures;
    }

    @Override
    public int hashCode() {
        return useSignatures;
    }
}
