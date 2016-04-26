package bftsmart.demo.adapt.TTP;

public interface MessageListener<T> {
    void onMessageReceived(T message);
}
