package space.gavinklfong.demo.banking.utils;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.models.Transaction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class TransactionCsvReaderTest {

    @Test
    void testTransactionCsvReader() throws URISyntaxException, IOException, CsvException {
        URL fileUrl = getClass().getClassLoader().getResource("transactions.csv");
        String fullPath = Paths.get(fileUrl.toURI()).toFile().getAbsolutePath();

        List<Transaction> transactions = TransactionCsvReader.readTransactionsFromCSV(fullPath);
        transactions.forEach(transaction -> log.info("{}", transaction));
        assertThat(transactions).hasSizeGreaterThan(0);
    }
}
