package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.StatusMessage;

import java.util.List;

public interface AdaptPolicy<T extends StatusMessage> {
    void execute(List<T> messages);
}
