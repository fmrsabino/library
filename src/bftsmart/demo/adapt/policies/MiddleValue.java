package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.StatusMessage;

import java.util.List;

/**
 * Sorts Status messages in ascending threat order and chooses the middle one
 */
public class MiddleValue implements AdaptPolicy<StatusMessage> {
    @Override
    public void execute(List<StatusMessage> messages) {
        int targetPosition = messages.size() / 2;
        StatusMessage sm = messages.get(targetPosition);
        System.out.println(sm);
    }
}
