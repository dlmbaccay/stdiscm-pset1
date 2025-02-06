import java.util.concurrent.*;

public class A1B1 {
    // Immediate Printing, Search Range

    public static void run(int threads, int limit) {
        System.out.println("\n Running A1B1 with " + threads + " threads and limit " + limit);

        String startTime = Main.getTimestamp();
        System.out.println("\n Start Time: " + startTime + "\n");

        // Create a thread pool with the specified number of threads
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Create a latch to wait for all tasks to complete
        CountDownLatch latch = new CountDownLatch(threads);

        // For each thread, calculate the range of numbers to check for primality
        int range = limit / threads;

        for (int i = 0; i < threads; i++) {
            int start = i * range + 1;
            int end = (i == threads - 1) ? limit : (i + 1) * range;
            int threadId = i + 1;

            // Submit a task to the executor for checking primality of numbers within thread range
            executor.submit(() -> {
                try {
                    for (int num = start; num <= end; num++) {
                        if (isPrime(num)) {
                            synchronized (System.out) {
                                System.out.println(" Thread " + threadId + " found prime: " + num + " at " + Main.getTimestamp());
                            }
                        }
                    }
                } finally {
                    // Decrement the latch count, indicating that the thread has completed
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

        String endTime = Main.getTimestamp();
        System.out.println("\n End Time: " + endTime);

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
