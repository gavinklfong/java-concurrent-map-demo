package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.models.Transaction;
import space.gavinklfong.demo.banking.models.TransactionType;
import space.gavinklfong.demo.banking.utils.AccountCsvReader;
import space.gavinklfong.demo.banking.utils.TransactionCsvReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public abstract class BankingServiceBaseTest {

    private static final String ACCOUNT_NUMBER = "001-123456-001";
    private static String TXNS_DATA_FILE = "";

    protected BankingService bankingService;

    @BeforeAll
    static void beforeAll() throws URISyntaxException {
        TXNS_DATA_FILE = BankingServiceBaseTest.class.getClassLoader().getResource("transactions.csv").getPath();
    }

    @Test
    void testConcurrentTransactionExecution() throws IOException, CsvException, InterruptedException {

        Map<String, List<Transaction>> accountTransactionMap = TransactionCsvReader.readTransactionsFromCSV(TXNS_DATA_FILE);
        Map.Entry<String, List<Transaction>> entry = accountTransactionMap.entrySet().stream().findFirst().get();

        // calculate expected balance
        double transactionSum = calculateTransactionSum(entry.getValue());
        double expectedBalance = bankingService.getBalance(entry.getKey()) + transactionSum;

        // partition the transactions into 3 parts
        List<List<Transaction>> transactionPartitions = ListUtils.partition(entry.getValue(), 3);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        long startTime = System.nanoTime();
        transactionPartitions.forEach(partition -> {
            executorService.execute(() -> {
                doTransactions(partition);
            });
        });
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            log.warn("Thread execution timeout while await for termination");
        }

        log.info("execution time: {}", (System.nanoTime() - startTime));


        // assert account balance
        assertThat(bankingService.getBalance(entry.getKey()))
                .describedAs("Check account [%s] balance should be [%f]", entry.getKey(), expectedBalance)
                .isEqualTo(expectedBalance);
    }

    private void doTransactions(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            bankingService.doTransaction(transaction);
        });
    }

    private double calculateTransactionSum(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(transaction ->
                        (transaction.getTransactionType()
                                .equals(TransactionType.WITHDRAW))?
                                transaction.getAmount() * (-1) :
                                transaction.getAmount()
                )
                .sum();
    }

    private void testTransactions(String accountNumber, List<Transaction> transactions) {
        // calculate expected balance
        double transactionSum = calculateTransactionSum(transactions);

        double expectedBalance = bankingService.getBalance(accountNumber) + transactionSum;

        // run transactions
        transactions.forEach(bankingService::doTransaction);

        // assert account balance
        assertThat(bankingService.getBalance(accountNumber))
                .describedAs("Check account [%s] balance should be [%d]", accountNumber, expectedBalance)
                .isEqualTo(expectedBalance);
    }

    protected Map<String, Account> initializeAccountMap() throws IOException, CsvException {
        String accountFilePath = getClass().getClassLoader().getResource("accounts.csv").getPath();
        return AccountCsvReader.readAccountsFromCSV(accountFilePath);
    }
}
