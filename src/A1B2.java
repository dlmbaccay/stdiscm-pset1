import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class A1B2 {
    // Immediate Printing, Linear Search

    public static void run(int threads, int limit) {
        System.out.println("\n Running A1B2 with " + threads + " threads and limit " + limit + " at " + Main.getTimestamp());
        System.out.println("\n Start Time: " + Main.getTimestamp() + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        ReentrantLock lock = new ReentrantLock();
        CountDownLatch latch = new CountDownLatch(limit - 1);

        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;

            executor.submit(() -> {
                try {
                    if (isPrime(currentNum)) {
                        try {
                            lock.lock();
                            int threadId = (int) Thread.currentThread().threadId();
                            System.out.println(" Thread " + (threadId % threads + 1) + " found prime: " + currentNum + " at " + Main.getTimestamp());
                        } finally {
                            lock.unlock();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        executor.shutdown();

        try {
            latch.await();
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("\n End Time: " + Main.getTimestamp());
    }

    public static boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i <= Math.sqrt(num); i++)
            if (num % i == 0) return false;

        return true;
    }
}