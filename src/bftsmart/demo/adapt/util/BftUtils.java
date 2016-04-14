package bftsmart.demo.adapt.util;

public class BftUtils {
    public static int getQuorum(int f) {
        return 2 * f + 1;
    }

    public static int getF(int n) {
        return (n - 1) / 3;
    }
}
