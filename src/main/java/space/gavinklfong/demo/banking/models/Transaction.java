package space.gavinklfong.demo.banking.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@FieldDefaults(makeFinal = true, level= AccessLevel.PRIVATE)
public class Transaction {
    String accountNumber;
    TransactionType transactionType;
    Double amount;
}
