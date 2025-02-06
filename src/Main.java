import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static boolean isRunning = true;
    static Scanner scan = new Scanner(System.in);
    static int threads = 0;
    static int limit = 0;

    public static void configFallback() {
        System.out.println("\n Invalid thread and limit configuration. Please enter the number of threads and limit manually.");

        System.out.print("\n Enter the number of threads: ");
        String threadInput = scan.next();

        while (!threadInput.matches("\\d+") || Integer.parseInt(threadInput) < 1) {
            System.out.print(" Invalid input. Please enter a number greater than 0: ");
            threadInput = scan.next();
        }

        threads = Integer.parseInt(threadInput);

        System.out.print("\n Enter the limit: ");
        String limitInput = scan.next();

        while (!limitInput.matches("\\d+") || Integer.parseInt(limitInput) < 2) {
            System.out.print(" Invalid input. Please enter a number greater than 1: ");
            limitInput = scan.next();
        }

        limit = Integer.parseInt(limitInput);
    }

    public static String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
    }

    public static long getDuration(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);

        return java.time.Duration.between(start, end).toMillis();
    }

    public static boolean isPrime(int num) {
        if (num < 2) return false;

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) return false;
        }

        return true;
    }

    public static void promptUI() {
        System.out.printf("""
            \n ======================================================================
            \n Threaded Prime Number Search
            \s
             Threads: %d
             Limit: %d
            \s
             Choose a variant to execute
             1 - Immediate Printing, Search Range
             2 - Deferred Printing, Search Range
             3 - Immediate Printing, Linear Search
             4 - Deferred Printing, Linear Search
             5 - Exit Application
            """, threads, limit);
    }

    public static void main(String[] args) {
        String filePath = "src/config.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String threadInput = reader.readLine();
            String limitInput = reader.readLine();
            reader.close();

            if (threadInput == null || limitInput == null) {
                throw new IOException("Invalid configuration values");
            } else if (!threadInput.matches("\\d+") || !limitInput.matches("\\d+")) {
                throw new IOException("Invalid configuration values");
            } else if (Integer.parseInt(threadInput) < 1 || Integer.parseInt(limitInput) < 2) {
                throw new IOException("Invalid configuration values");
            }

            threads = Integer.parseInt(threadInput);
            limit = Integer.parseInt(limitInput);
        } catch (IOException e) {
            configFallback();
        }

        while (isRunning) {
            promptUI();

            if (threads > limit) {
                System.out.println("\n Number of threads is greater than the limit. Adjusting threads to " + limit);
                threads = limit;
            }

            System.out.print("\n Enter your choice: ");
            String input = scan.next();

            do {
                try {
                    Integer.parseInt(input);

                    if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 5) {
                        System.out.print(" Invalid choice. Enter a number from 1 to 5: ");
                        input = scan.next();
                    }
                } catch (NumberFormatException e) {
                    System.out.print(" Invalid choice. Enter a number from 1 to 5: ");
                    input = scan.next();
                }
            } while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("5"));

            switch (input) {
                case "1" -> A1B1.run(threads, limit);   // Immediate Printing, Search Range
                case "2" -> A2B1.run(threads, limit);   // Deferred Printing, Search Range
                case "3" -> A1B2.run(threads, limit);   // Immediate Printing, Linear Search
                case "4" -> A2B2.run(threads, limit);   // Deferred Printing, Linear Search
                case "5" -> {
                    System.out.println("\n Exiting application...");
                    System.exit(0);
                }
                default -> System.out.println(" Invalid choice. Please enter a number from 1 to 5.");
            }

            System.out.println("\n ======================================================================");
            System.out.print("\n Do you want to run another variant? (y/n): ");
            String choice = scan.next();

            do {
                if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
                    System.out.print(" Invalid choice. Please enter 'y' or 'n': ");
                    choice = scan.next();
                }
            } while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

            if (choice.equalsIgnoreCase("n")) {
                System.out.println("\n Exiting application...");
                isRunning = false;
            }
        }
    }
}