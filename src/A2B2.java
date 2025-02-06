import java.util.concurrent.*;
import java.util.*;
import java.util.stream.*;

public class A2B2 {
    // Deferred Printing, Linear Search

    public static void run(int threads, int limit) {
        System.out.println("\n Running A2B2 with " + threads + " threads and limit " + limit);
        System.out.println("\n Start Time: " + Main.getTimestamp() + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(limit - 1);
        List<Set<Integer>> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threads; i++) {
            results.add(Collections.synchronizedSet(new TreeSet<>()));
        }

        for (int num = 2; num <= limit; num++) {
            final int currentNum = num;

            executor.submit(() -> {
                try {
                    if (isPrime(currentNum)) {
                        int threadId = (int) Thread.currentThread().threadId();
                        results.get(threadId % threads).add(currentNum);
                    }
                } finally {
                    latch.countDown();
                }
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

        for (int threadId = 0; threadId < threads; threadId++) {
            System.out.print(" Thread " + (threadId + 1) + " found: ");
            if (results.get(threadId).isEmpty()) {
                System.out.println();
            } else {
                System.out.println(results.get(threadId).stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")));
            }
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
