import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class A1B2 {
    // Immediate Printing, Linear Search

    public static void run(int threads, int limit) {
        System.out.println("\n Running A1B2 with " + threads + " threads and limit " + limit);

        String startTime = Main.getTimestamp();
        System.out.println("\n Start Time: " + startTime + "\n");

        // Create a thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a latch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(limit - 1);

        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;
            final AtomicBoolean isPrime = new AtomicBoolean(true); // isPrime flag PER NUMBER

            // Check if the number is prime by checking divisibility by numbers up to sqrt(num)
            // sqrt(num) is used as the upper limit since factors repeat after this point
            for (int divisor = 2; divisor <= Math.sqrt(currentNum); divisor++) {
                final int finalDivisor = divisor;

                // Check if the number is divisible by the current divisor
                executor.submit(() -> {
                    if (currentNum % finalDivisor == 0) {
                        isPrime.set(false);
                    }
                });

                // If the number is not prime, break the loop
                if (!isPrime.get()) {
                    break;
                }
            }

            // Immediately print the number if it is prime
            executor.submit(() -> {
                if (isPrime.get()) {
                    synchronized (System.out) {
                        System.out.println(" | " + Main.getTimestamp() + " | Thread " + (Thread.currentThread().threadId() % threads + 1) + " found prime: " + currentNum);
                    }
                }

                latch.countDown(); // Decrement latch after all divisibility checks for this number are complete
            });
        }

        executor.shutdown();

        try {
            latch.await(); // Wait for all tasks to complete
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        String endTime = Main.getTimestamp();
        System.out.println("\n End Time: " + endTime);

        long duration = Main.getDuration(startTime, endTime);
        System.out.println("\n Duration: " + duration + " ms");
    }
}