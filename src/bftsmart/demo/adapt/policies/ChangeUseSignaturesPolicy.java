package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.adapt.ChangeUseSignaturesMessage;
import bftsmart.demo.adapt.messages.sensor.SignaturesMessage;
import bftsmart.demo.adapt.util.BftUtils;

public class ChangeUseSignaturesPolicy implements AdaptPolicy<SignaturesMessage> {
    @Override
    public void execute(int executorId, SignaturesMessage message) {
        ChangeUseSignaturesMessage msg = new ChangeUseSignaturesMessage(message.getUseSignatures());
        BftUtils.sendMessage(executorId, "", msg);
    }
}
