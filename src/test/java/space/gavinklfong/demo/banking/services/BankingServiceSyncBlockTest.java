package space.gavinklfong.demo.banking.services;

import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

@Slf4j
public class BankingServiceSyncBlockTest extends BankingServiceBaseTest {

    @BeforeEach
    void setup() throws IOException, CsvException {
        bankingService = new BankingServiceSyncBlockImpl(initializeAccountMap());
    }

}
