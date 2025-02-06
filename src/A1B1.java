import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class A1B1 {
    // Immediate Printing, Search Range

    public static void run(int threads, int limit) {
        System.out.println("\n Running A1B1 with " + threads + " threads and limit " + limit);
        System.out.println("\n Start Time: " + Main.getTimestamp() + "\n");

        ReentrantLock lock = new ReentrantLock();
        CountDownLatch latch = new CountDownLatch(threads);
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        int range = limit / threads;

        for (int i = 0; i < threads; i++) {
            int start = i * range + 1;
            int end = (i == threads - 1) ? limit : (i + 1) * range;
            int threadId = i + 1;

            executor.submit(() -> {
                try {
                    for (int num = start; num <= end; num++) {
                        if (isPrime(num)) {
                            try {
                                lock.lock();
                                System.out.println("Thread " + threadId + " found prime: " + num + " at " + Main.getTimestamp());
                            } finally {
                                lock.unlock();
                            }
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
