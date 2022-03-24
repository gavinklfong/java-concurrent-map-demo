package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import space.gavinklfong.demo.banking.models.Account;
import space.gavinklfong.demo.banking.utils.AccountCsvReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringJUnitConfig
public class BankingServiceTest {

    @TestConfiguration
    public static class DataConfiguration {
        @Bean
        public BankingService bankingService() throws URISyntaxException, IOException, CsvException {
            URL accountFileUrl = getClass().getClassLoader().getResource("accounts.csv");
            String fullPath = Paths.get(accountFileUrl.toURI()).toFile().getAbsolutePath();
            Map<String, Account> accounts = AccountCsvReader.readAccopuntsFromCSV(fullPath);

            return new BankingService(accounts);
        }
    }

    private static final String ACCOUNT_NUMBER = "001-123456-001";

    @Autowired
    private BankingService bankingService;

    @Test
    void testBalance() {
        Double balance = bankingService.getBalance(ACCOUNT_NUMBER);
        log.info("balance: {}", balance);
        assertThat(balance).isEqualTo(10000D);
    }

    @Test
    void testDeposit() {
        Double originalBalance = bankingService.getBalance(ACCOUNT_NUMBER);
        log.info("original balance: {}", originalBalance);
        bankingService.deposit(ACCOUNT_NUMBER, 200D);
        Double updatedBalance = bankingService.getBalance(ACCOUNT_NUMBER);
        log.info("updated balance: {}", updatedBalance);
        Double diff = updatedBalance - originalBalance;
        assertThat(updatedBalance).isGreaterThan(originalBalance);
        assertThat(diff).isEqualTo(200D);
    }

    @Test
    void testWithdraw() {
        Double originalBalance = bankingService.getBalance(ACCOUNT_NUMBER);
        log.info("original balance: {}", originalBalance);
        bankingService.withdraw(ACCOUNT_NUMBER, 200D);
        Double updatedBalance = bankingService.getBalance(ACCOUNT_NUMBER);
        log.info("updated balance: {}", updatedBalance);
        Double diff = updatedBalance - originalBalance;
        assertThat(updatedBalance).isLessThan(originalBalance);
        assertThat(diff).isEqualTo(-200D);
    }

}
