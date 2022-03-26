package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import space.gavinklfong.demo.banking.models.Account;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class BankingServiceConcurrentMapTest extends BankingServiceBaseTest{

    @BeforeEach
    void setup() throws IOException, CsvException {
        Map<String, Account> accounts = new ConcurrentHashMap<>(initializeAccountMap());
        bankingService = new BankingServiceImpl(accounts);
    }

}
