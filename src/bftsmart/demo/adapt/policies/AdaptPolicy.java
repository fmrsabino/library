package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.AdaptMessage;

import java.util.List;

public interface AdaptPolicy<T extends AdaptMessage> {
    void execute(List<T> messages);
}
