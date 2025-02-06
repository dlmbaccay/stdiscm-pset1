import java.util.concurrent.*;
import java.util.*;

public class A2B2 {
    public static void run(int threads, int limit) {
        System.out.println("\n Running A2B2 with " + threads + " threads and limit " + limit);

        String startTime = Main.getTimestamp();
        System.out.println("\n Start Time: " + startTime + "\n");

        // Create a thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a latch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(limit - 1);

        // Create a list to store prime numbers found
        List<Integer> primeResults = Collections.synchronizedList(new ArrayList<>());

        // Loop through numbers from 2 to limit
        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;

            // Submit number as task to the executor for primality checking
            executor.submit(() -> {
                try {
                    if (isPrime(currentNum)) { // Add prime number to the list
                        primeResults.add(currentNum);
                    }
                } finally {
                    // Decrement the latch count, indicating that the number has been checked for primality
                    latch.countDown();
                }
            });
        }

        executor.shutdown();

        try {
            latch.await(); // Wait for all tasks to complete
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) { // Fallback in case of timeout
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // Print results in sorted order
        Collections.sort(primeResults);
        System.out.println(" Prime Numbers from 1 to " + limit + ":\n");

        for (int i = 0; i < primeResults.size(); i++) {
            System.out.print(" " + primeResults.get(i) + " ");

            // line break every 10 primes
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }

        String endTime = Main.getTimestamp();
        System.out.println("\n\n End Time: " + endTime);

        long duration = Main.getDuration(startTime, endTime);
        System.out.println("\n Duration: " + duration + " ms");
    }

    public static boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }

        return true;
    }
}
