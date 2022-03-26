package space.gavinklfong.demo.banking.services;

import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.domain.TestMappings;
import org.jsmart.zerocode.jupiter.extension.ParallelLoadExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@LoadWith("load_test.properties")
@ExtendWith({ParallelLoadExtension.class})
public class BankingServiceLoadTest {

    @Test
    @TestMappings({
            @TestMapping(testClass = BankingServiceConcurrentMapTest.class, testMethod = "testConcurrentTransactionExecution"),
    })
    public void runLoadTest() {
        // This space remains empty
    }
}
