import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class A2B1 {
    // Deferred Printing, Search Range

    public static void run(int threads, int limit) {
        System.out.println("\n Running A2B1 with " + threads + " threads and limit " + limit);
        System.out.println("\n Start Time: " + Main.getTimestamp() + "\n");

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);
        List<List<Integer>> results = Collections.synchronizedList(new ArrayList<>());

        int range = limit / threads;

        for (int i = 0; i < threads; i++) {
            results.add(Collections.synchronizedList(new ArrayList<>()));
        }


        for (int i = 0; i < threads; i++) {
            int start = i * range + 1;
            int end = (i == threads - 1) ? limit : (i + 1) * range;
            int threadId = i + 1;

            executor.submit(() -> {
                try {
                    for (int num = start; num <= end; num++) {
                        if (isPrime(num)) {
                            results.get(threadId - 1).add(num);
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

        synchronized (System.out) {
            for (int i = 0; i < results.size(); i++) {
                List<Integer> threadPrimes = results.get(i);
                if (!threadPrimes.isEmpty()) {
                    System.out.println(" Thread " + (i + 1) + " primes: " + threadPrimes.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(" ")));
                }
            }
        }

        System.out.println("\n End Time: " + Main.getTimestamp());
    }

    private static boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i <= Math.sqrt(num); i++)
            if (num % i == 0) return false;

        return true;
    }
}