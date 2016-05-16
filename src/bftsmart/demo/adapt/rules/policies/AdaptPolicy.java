package bftsmart.demo.adapt.rules.policies;

import bftsmart.demo.adapt.util.Registry;

public interface AdaptPolicy {
    void execute(int executorId, Registry messages);
}
