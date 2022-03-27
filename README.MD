# Java Demo - Synchronization On Shared Data Access

## Introduction
This project provides a list of options on how to apply synchronization on shared data in a multi-threaded environment.

The demo is built based on a example banking system. Multiple threads execute deposit / withdraw transactions concurrently. With the techniques of synchronization, it shows that the system keeps a consist account balance.

![Banking System Diagram](https://raw.githubusercontent.com/gavinklfong/java-concurrent-map-demo/main/blob/01-banking-system.png)

The following list of synchronziation techiques is covered in this project:
- Apply synchronization to method call 
- Synchronized map wrapper 
- Hashtable 
- ConcurrentMap 
- Apply synchronization to a block of code

## Run the demo
Refer to test code for the demonstration. Run the test classes and they should all pass except the test class for HashMap.

## Run the load test
Run `BankingServiceLoad.class` in the test code for the load test execution. 

Follow the steps below if you would like to run the load test with different transaction volume:
1. Adjust constants in `data-generator/generate-transactions.js` and run the script.
2. Run data generation script `node generate-transactions.js`
3. Copy the generated data file from `data-generator/data/transactions.csv` to `src/test/resources`
4. Run `BankingServiceLoad.class`