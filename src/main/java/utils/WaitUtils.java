package utils;

public final class WaitUtils {
    private WaitUtils() {}

    public static void sleep(int milliseconds) {
        if (milliseconds <= 0) return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

