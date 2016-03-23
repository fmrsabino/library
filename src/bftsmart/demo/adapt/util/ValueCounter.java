package bftsmart.demo.adapt.util;

public class ValueCounter<T> extends Pair<Integer, T> {
    public ValueCounter(Integer counter, T value) {
        super(counter, value);
    }

    public void incrementCounter() {
        setFirst(getFirst() + 1);
    }

    public Integer getCounter() {
        return getFirst();
    }

    public T getValue() {
        return getSecond();
    }
}
