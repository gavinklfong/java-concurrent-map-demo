package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;

@Slf4j
public class BankingServiceHashTableTest extends BankingServiceBaseTest {

    @BeforeEach
    void setup() throws IOException, CsvException {
        bankingService = new BankingServiceImpl(
                new Hashtable<>(initializeAccountMap())
        );
    }
}
