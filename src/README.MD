# Threaded Prime Number Search

### GitHub Repository: [Threaded Prime Number Search](https://github.com/dlmbaccay/stdiscm-pset1)

### By: [Dominic Baccay](https://github.com/dlmbaccay)

## Overview
This application is designed to search for prime numbers using multiple threads. It allows users to configure the number of threads and the range limit for the search. The application provides different variants for immediate and deferred printing of prime numbers.

## Build / Compilation Instructions

### Prerequisites
- Java Development Kit (JDK) 21.0.1 or later
- IntelliJ IDEA 2024.3.2.2 or any other compatible IDE

### Steps to Build and Compile

1. **Clone the Repository**
   ```sh
   git clone https://github.com/dlmbaccay/stdiscm-pset1.git ./stdiscm-pset1
   cd stdiscm-pset1
   ```

2. Open the Project in IntelliJ IDEA
   - Open IntelliJ IDEA
   - Click on `Open or Import`
   - Select the project directory
   - Click `Open`


3. **Build the Project**
   - Click on `Build` 
   - Click on `Build Project`


4. **Run the Application**
    - Click on `Run`
    - Click on `Run 'Main'`


## Usage Instructions

1. Configure Threads and Limit
   - The application will attempt to read the configuration from the `config.txt` file
   - If the configuration is invalid or missing, you will be prompted to enter the number of threads and the limit manually.
   - The file should contain two integers separated by a line break
     - The first integer is the number of threads
     - The second integer is the range limit for the search
   - Example:
     ```
     4
     200
     ```
     
2. Choose a Variant
   - Follow the on-screen instructions to choose a variant for the prime number search:
     - 1: Immediate Printing, Search Range
     - 2: Deferred Printing, Search Range
     - 3: Immediate Printing, Linear Search
     - 4: Deferred Printing, Linear Search
     - 5: Exit Application
     
   
3. View Results
   - The application will display the prime numbers found by each thread along with timestamps.
   - The total duration of the search will also be displayed.

## Notes
- `ExecutorService` was used to manage threads and execute tasks.
- `CountDownLatch` was used on all variants to ensure that all threads have completed before calculating the total duration.
- The `synchronizedList` from the `Collections` class was used to ensure thread safety when adding prime numbers to the list on the deferred printing variants.
- The third variant uses the `ThreadLocal` class to print the thread ID alongside the prime numbers found by each thread.
- `synchronized` blocks are used to ensure thread safety when printing prime numbers on the immediate printing variants.