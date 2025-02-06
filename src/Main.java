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
        // todo: Implement user input fallback when config file is not found or invalid
    }

    public static String getTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
    }

    public static void promptUI() {
        System.out.printf("""
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
             \n Enter your choice:\s""", threads, limit);
    }

    public static void main(String[] args) {
        String filePath = "src/config.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            threads = Integer.parseInt(reader.readLine());
            limit = Integer.parseInt(reader.readLine());

            reader.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            // configFallback();
        }

        while (isRunning) {
            promptUI();

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

            System.out.print("\n Do you want to run another variant? (y/n): ");

            String choice = scan.next();

            do {
                if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
                    System.out.print(" Invalid choice. Please enter 'y' or 'n': ");
                    choice = scan.next();
                }
            } while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));

            if (choice.equalsIgnoreCase("n")) isRunning = false;
        }


    }
}