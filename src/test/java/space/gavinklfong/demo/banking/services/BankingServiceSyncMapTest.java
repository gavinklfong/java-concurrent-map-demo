package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class BankingServiceSyncMapTest extends BankingServiceBaseTest {

    @BeforeEach
    void setup() throws IOException, CsvException {
        bankingService = new BankingServiceImpl(
                Collections.synchronizedMap(initializeAccountMap())
        );
    }
}
