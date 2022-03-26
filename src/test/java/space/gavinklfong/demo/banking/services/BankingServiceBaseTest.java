package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private static final int THREAD_COUNT = 3;

    private static Map<String, List<Transaction>> accountTransactionMap = null;

    protected BankingService bankingService;

    @BeforeAll
    static void beforeAll() throws URISyntaxException, IOException, CsvException {
        String dataFile = BankingServiceBaseTest.class.getClassLoader().getResource("transactions.csv").getPath();
        accountTransactionMap = TransactionCsvReader.readTransactionsFromCSV(dataFile);
    }

    @RepeatedTest(10)
    void testConcurrentTransactionExecution() throws IOException, CsvException, InterruptedException {

        Map.Entry<String, List<Transaction>> entry = accountTransactionMap.entrySet().stream().findFirst().get();

        // calculate expected balance
        double transactionSum = calculateTransactionSum(entry.getValue());
        double expectedBalance = bankingService.getBalance(entry.getKey()) + transactionSum;

        // partition the transactions into 3 parts
        List<List<Transaction>> transactionPartitions = ListUtils.partition(entry.getValue(), entry.getValue().size() / THREAD_COUNT);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

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

        log.info("execution time: {} ms", (System.nanoTime() - startTime) / 1_000_000);


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


    protected Map<String, Account> initializeAccountMap() throws IOException, CsvException {
        String accountFilePath = getClass().getClassLoader().getResource("accounts.csv").getPath();
        return AccountCsvReader.readAccountsFromCSV(accountFilePath);
    }
}
