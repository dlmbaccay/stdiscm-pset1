import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class A2B2 {
    // Deferred Printing, Linear Search

    public static void run(int threads, int limit) {
        System.out.println("\n Running A2B2 with " + threads + " threads and limit " + limit);

        String startTime = Main.getTimestamp();
        System.out.println("\n Start Time: " + startTime + "\n");

        // Create a thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a latch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(limit - 1);

        // Create a list to store prime numbers found by each thread, since printing is deferred
        List<Integer> primeResults = Collections.synchronizedList(new ArrayList<>());

        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;
            final AtomicBoolean isPrime = new AtomicBoolean(true); // isPrime flag PER NUMBER

            // Check if the number is prime by checking divisibility by numbers up to sqrt(num)
            // sqrt(num) is used as the upper limit since factors repeat after this point
            for (int divisor = 2; divisor <= Math.sqrt(currentNum); divisor++) {
                final int finalDivisor = divisor;

                executor.submit(() -> {
                    if (currentNum % finalDivisor == 0) {
                        isPrime.set(false);
                    }
                });

                if (!isPrime.get()) {
                    break;
                }
            }

            executor.submit(() -> {
                if (isPrime.get()) {
                    primeResults.add(currentNum);
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

        // Print results in sorted order
        Collections.sort(primeResults);
        System.out.println(" | Prime Numbers from 1 to " + limit + ":\n");

        for (int i = 0; i < primeResults.size(); i++) {
            System.out.print(" " + primeResults.get(i) + " ");

            // line break every 15 primes
            if ((i + 1) % 15 == 0) {
                System.out.println();
            }
        }

        String endTime = Main.getTimestamp();
        System.out.println("\n\n End Time: " + endTime);

        long duration = Main.getDuration(startTime, endTime);
        System.out.println("\n Duration: " + duration + " ms");
    }
}