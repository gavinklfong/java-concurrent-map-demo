package space.gavinklfong.demo.banking.utils;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.banking.models.Account;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AccountCsvReaderTest {

    @Test
    void testAccountCsvReader() throws URISyntaxException, IOException, CsvException {
        String fullPath = getClass().getClassLoader().getResource("accounts.csv").getPath();

        Map<String, Account> accounts = AccountCsvReader.readAccountsFromCSV(fullPath);
        log.info("accounts: {}", accounts);
        assertThat(accounts).hasSizeGreaterThan(0);
    }
}
