package bftsmart.demo.adapt.util;

public abstract class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    protected T getFirst() {
        return first;
    }

    protected U getSecond() {
        return second;
    }

    protected void setFirst(T first) {
        this.first = first;
    }

    protected void setSecond(U second) {
        this.second = second;
    }
}
