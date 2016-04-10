package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.StatusMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sorts Status messages in ascending threat order and chooses the middle one
 */
public class MiddleValue implements AdaptPolicy<StatusMessage> {
    @Override
    public void execute(List<StatusMessage> messages) {
        messages = messages.stream().sorted((sm1, sm2) -> sm1.getThreatLevel() - sm2.getThreatLevel()).collect(Collectors.toList());
        messages.stream().map(StatusMessage::getThreatLevel).forEach(System.out::println);

        int targetPosition = messages.size() / 2;
        StatusMessage sm = messages.get(targetPosition);
        System.out.println(sm);
    }
}