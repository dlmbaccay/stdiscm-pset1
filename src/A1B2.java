import java.util.concurrent.*;

public class A1B2 {
    // Immediate Printing, Linear Search

    public static void run(int threads, int limit) {
        System.out.println("\n Running A2B2 with " + threads + " threads and limit " + limit);
        System.out.println("\n Start Time: " + Main.getTimestamp() + "\n");

        // Create a thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a latch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(limit - 1);

        // Assign each thread a number from 1 to threads, for printing purposes
        ThreadLocal<Integer> threadIndex = ThreadLocal.withInitial(() -> (int) (Thread.currentThread().threadId() % threads) + 1);

        // Loop through numbers from 2 to limit
        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;

            // Submit number as task to the executor for primality checking
            executor.submit(() -> {
                try {
                    if (isPrime(currentNum)) {
                        synchronized (System.out) {
                            int assignedThread = threadIndex.get();
                            System.out.println(" Thread " + assignedThread + " found prime: " + currentNum + " at " + Main.getTimestamp());
                        }
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

        System.out.println("\n End Time: " + Main.getTimestamp());
    }

    public static boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }

        return true;
    }
}
